/*
 * Scriptographer
 *
 * This file is part of Scriptographer, a Scripting Plugin for Adobe Illustrator
 * http://scriptographer.org/
 *
 * Copyright (c) 2002-2010, Juerg Lehni
 * http://scratchdisk.com/
 *
 * All rights reserved. See LICENSE file for details.
 * 
 * File created on Jul 9, 2009.
 */

package com.scriptographer.ai;

import com.scratchdisk.script.ChangeReceiver;
import com.scriptographer.list.AbstractStructList;

/**
 * @author lehni
 *
 */
public class Artboard extends AbstractStructList.Entry<Document> implements ChangeReceiver {

	/**
	 * The bounds of the artboard in points in document coordinate system
	 */
	private Rectangle bounds;

	/**
	 * Show center mark
	 */
	private boolean showCenter;

	/**
	 * Show cross hairs
	 */
	private boolean showCrossHairs;

	/** 
	 * Show title and action safe areas (for video) 
	 */
	private boolean showSafeAreas;

	/**
	 * Pixel aspect ratio, used in ruler visualization if the units are pixels
	 */
	private double pixelAspectRatio;

	/**
	 * Creates a new Artboard in the document.
	 * 
	 * @param bounds the bounding rectangle of the Artboard
	 * @param showCenter {@default false}
	 * @param showCrossHairs {@default false}
	 * @param showSafeAreas {@default false}
	 * @param pixelAspectRatio {@default 1}
	 */
	public Artboard(Rectangle bounds, boolean showCenter,
			boolean showCrossHairs, boolean showSafeAreas,
			double pixelAspectRatio) {
		set(bounds, showCenter, showCrossHairs, showSafeAreas, pixelAspectRatio);
	}

	public Artboard(Rectangle bounds, boolean showCenter,
			boolean showCrossHairs, boolean showSafeAreas) {
		this(bounds, showCenter, showCrossHairs, showSafeAreas, 1);
	}

	public Artboard(Rectangle bounds, boolean showCenter,
			boolean showCrossHairs) {
		this(bounds, showCenter, showCrossHairs, false, 1);
	}

	public Artboard(Rectangle bounds, boolean showCenter) {
		this(bounds, showCenter, false, false, 1);
	}

	public Artboard(Rectangle bounds) {
		this(bounds, false, false, false, 1);
	}

	public Artboard() {
		this(new Rectangle(), false, false, false, 1);
	}

	protected Artboard(Document document, int index) {
		super(document, index);
	}

	/**
	 * Called from the native side
	 */
	protected void set(Rectangle bounds, boolean showCenter,
			boolean showCrossHairs, boolean showSafeAreas,
			double pixelAspectRatio) {
		this.bounds = bounds;
		this.showCenter = showCenter;
		this.showCrossHairs = showCrossHairs;
		this.showSafeAreas = showSafeAreas;
		this.pixelAspectRatio = pixelAspectRatio;
	}

	protected boolean nativeGet() {
		return ArtboardList.nativeGet(reference.handle, index, this);
	}

	protected boolean nativeInsert() {
		return ArtboardList.nativeInsert(reference.handle, index, bounds, showCenter,
				showCrossHairs, showSafeAreas, pixelAspectRatio);
	}

	protected boolean nativeSet() {
		return ArtboardList.nativeSet(reference.handle, index, bounds, showCenter,
				showCrossHairs, showSafeAreas, pixelAspectRatio);
	}

	/**
	 * The bounding rectangle of the artboard.
	 */
	public Rectangle getBounds() {
		update();
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		update();
		if (bounds != null) {
			this.bounds = bounds;
			markDirty();
		}
	}

	/**
	 * Specifies whether the center mark of the artboard is visible.
	 * @return {@true if the center mark is visible}
	 */
	public boolean getShowCenter() {
		update();
		return showCenter;
	}

	public void setShowCenter(boolean showCenter) {
		update();
		this.showCenter = showCenter;
		markDirty();
	}

	/**
	 * Specifies whether crosshairs are shown on the artboard.
	 * @return {@true if crosshairs are visible}
	 */
	public boolean getShowCrossHairs() {
		update();
		return showCrossHairs;
	}

	public void setShowCrossHairs(boolean showCrossHairs) {
		update();
		this.showCrossHairs = showCrossHairs;
		markDirty();
	}

	/**
	 * Specifies whether to show title and action safe areas (for video).
	 * @return {@true if title and action safe areas are visible}
	 */
	public boolean getShowSafeAreas() {
		update();
		return showSafeAreas;
	}

	public void setShowSafeAreas(boolean showSafeAreas) {
		update();
		this.showSafeAreas = showSafeAreas;
		markDirty();
	}

	/**
	 * The pixel aspect ratio which is used in ruler visualization if the units are pixels.
	 */
	public double getPixelAspectRatio() {
		update();
		return pixelAspectRatio;
	}

	public void setPixelAspectRatio(double pixelAspectRatio) {
		update();
		this.pixelAspectRatio = pixelAspectRatio;
		markDirty();
	}
}
