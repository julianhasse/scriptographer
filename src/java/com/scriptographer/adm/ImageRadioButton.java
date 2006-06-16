/*
 * Scriptographer
 * 
 * This file is part of Scriptographer, a Plugin for Adobe Illustrator.
 * 
 * Copyright (c) 2004-2005 Juerg Lehni, http://www.scratchdisk.com.
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
 * File created on 20.10.2005.
 * 
 * $RCSfile: ImageRadioButton.java,v $
 * $Author: lehni $
 * $Revision: 1.1 $
 * $Date: 2005/10/23 00:33:04 $
 */

package com.scriptographer.adm;

import java.awt.Insets;
import java.io.IOException;

public class ImageRadioButton extends RadioButton {

	public ImageRadioButton(Dialog dialog) {
		super(dialog, TYPE_PICTURE_RADIOBUTTON);
	}
	
	public Image getImage() {
		return super.getImage();
	}

	public void setImage(Object obj) throws IOException {
		super.setImage(obj);
	}

	public Image getRolloverImage() {
		return super.getRolloverImage();
	}

	public void setRolloverImage(Object obj) throws IOException {
		super.setRolloverImage(obj);
	}

	public Image getSelectedImage() {
		return super.getSelectedImage();
	}

	public void setSelectedImage(Object obj) throws IOException {
		super.setSelectedImage(obj);
	}

	public Image getDisabledImage() {
		return super.getDisabledImage();
	}

	public void setDisabledImage(Object obj) throws IOException {
		super.setDisabledImage(obj);
	}

	protected Insets getButtonInsets() {
		return INSETS_IMAGE;
	}
}