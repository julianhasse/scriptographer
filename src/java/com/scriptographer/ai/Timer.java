/*
 * Scriptographer
 * 
 * This file is part of Scriptographer, a Plugin for Adobe Illustrator.
 * 
 * Copyright (c) 2002-2007 Juerg Lehni, http://www.scratchdisk.com.
 * All rights reserved.
 *
 * Please visit http://scriptographer.com/ for updates and contact.
 * 
 * -- GPL LICENSE NOTICE --
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -- GPL LICENSE NOTICE --
 * 
 * File created on 02.04.2005.
 * 
 * $Id$
 */

package com.scriptographer.ai;

import java.util.ArrayList;
import java.util.Iterator;

import com.scriptographer.ScriptographerEngine; 
import com.scratchdisk.script.Callable;
import com.scratchdisk.util.IntMap;

/**
 * @author lehni
 */
public class Timer extends AIObject {
	public static final int TICKS_PER_SECONDS = 60;
	
	private boolean active;
	private boolean periodic;
	private int period;

	private static IntMap timers = new IntMap();
	private static ArrayList unusedTimers = null;
	private static int counter = 0;
	
	/**
	 * Creates a timer object.
	 * 
	 * @param period the timer's period in ticks. Multiply with
	 * {@link #TICKS_PER_SECONDS } to convert from seconds to ticks
	 * @param periodic
	 */
	public Timer(int period, boolean periodic) {
		// now see first wether there is an unusedEffect already:
		ArrayList unusedTimers = getUnusedTimers();
		
		int index = unusedTimers.size() - 1;
		if (index >= 0) {
			Timer timer = (Timer) unusedTimers.get(index);
			// found one, let's reuse it's handle and remove the old timer
			// from the list:
			handle = timer.handle;
			timer.handle = 0;
			unusedTimers.remove(index);
			setPeriod(period);
		} else {
			handle = nativeCreate("Scriptographer Timer " + (counter++), period);
			this.period = period;
		}		

		if (handle == 0)
			throw new RuntimeException("Unable to create Timer");

		active = false;
		this.periodic = periodic;
		
		timers.put(handle, this);
	}

	/* TODO: add this, change "int handle" constructor to "long handle"
	public Timer(int period) {
		this(period, true);
	}
	*/
	
	/**
	 * Called from the native environment.
	 */
	protected Timer(int handle) {
		this.handle = handle;	
	}
	
	private native int nativeCreate(String name, int period);
	
	public boolean start() {
		return setActive(true);
	}
	
	public boolean stop() {
		return setActive(false);
	}
	 
	public boolean setActive(boolean active) {
		if (active != this.active && nativeSetActive(handle, active)) {
			this.active = active;
			return true;
		}
		return false;
	}

	public boolean isActive() {
		return active;
	}
	
	public void dispose() {
		// see wether we're still linked:
		if (timers.get(handle) == this) {
			setActive(false);
			// if so remove it and put it to the list of unsed timers,
			// for later recycling
			timers.remove(handle);
			getUnusedTimers().add(this);
		}
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void setPeriod(int period) {
		if (nativeSetPeriod(handle, period))
			this.period = period;
	}
	
	public static void stopAll() {
		// Stop both used and unused timers:
		for (Iterator it = timers.values().iterator(); it.hasNext();)
			((Timer) it.next()).stop();
		ArrayList unused = getUnusedTimers();
		if (unused != null)
			for (Iterator it = unused.iterator(); it.hasNext();)
				((Timer) it.next()).stop();
	}
	
	public static void disposeAll() {
		// As remove() modifies the map, using an iterator is not possible here:
		Object[] timers = Timer.timers.values().toArray();
		for (int i = 0; i < timers.length; i++)
			((Timer) timers[i]).dispose();
	}
	
	private native boolean nativeSetActive(int handle, boolean active);
	
	private native boolean nativeSetPeriod(int handle, int period);

	private static ArrayList getUnusedTimers() {
		if (unusedTimers == null)
			unusedTimers = nativeGetTimers();
		return unusedTimers;
	}

	private static native ArrayList nativeGetTimers();

	private Callable onExecute = null;

	public void setOnExecute(Callable onExecute) {
		this.onExecute = onExecute;
	}
	
	public Callable getOnExecute() {
		return onExecute;
	}

	protected void onExecute() throws Exception {
		if (onExecute != null)
			ScriptographerEngine.invoke(onExecute, this);
	}

	/**
	 * To be called from the native environment:
	 */
	private static void onExecute(int handle) throws Exception {
		Timer timer = getTimer(handle);
		if (timer != null) {
			timer.onExecute();
			// simulate one shot timers:
			if (!timer.periodic)
				timer.stop();
		}
	}

	private static Timer getTimer(int handle) {
		return (Timer) timers.get(handle);
	}
	
	protected void finalize() {
		dispose();
	}
}
