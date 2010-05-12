//////////////////////////////////////////////////////////////////////////////
// Interface:

var values = {
	spacing: 20,
	scale: 'none',
}

var components = {
	spacing: { label: 'Spacing', units: 'percent', steppers: true },
	scale: { label: 'Scale', options: ['none', 'both', 'horizontal']}
};

var palette = new Palette('Clone Brush', components, values);

//////////////////////////////////////////////////////////////////////////////
// Mouse handling:

var items, nextItem, group;
function onMouseDown(event) {
	items = document.selectedItems.reverse();
	if(items.length) {
		prepareNextItem(0);
		group = new Group();
	} else {
		Dialog.alert('Please select one or more items first.');
	}
}

function onMouseDrag(event) {
	if(items.length) {
		var item = nextItem.clone();
		item.selected = false;
		item.position = event.middlePoint;

		group.appendTop(item);

		if(values.scale == 'both' || values.scale == 'horizontal') {
			var scale = event.delta.length / tool.minDistance;
			item.scale(values.scale == 'both' || values.scale == 'horizontal' ? scale : 1, values.scale == 'both' ? scale : 1);
		}

		// rotate the item by the angle of the vector that the mouse moved
		item.rotate(event.delta.angle);

		// setup distance threshold to be the width of the next item
		prepareNextItem(event.count);
	}
}

function onMouseUp(event) {
	if(group && group.children.length == 0)
		group.remove();
}

function prepareNextItem(count) {
	nextItem = items[count % (items.length)];
	// the amount of distance the mouse has to move
	// is equal to the width of the selected item plus the spacing
	var distance = nextItem.bounds.width * (1 + values.spacing / 100);
	tool.minDistance = distance;
	tool.maxDistance = values.scale == 'none' ? distance : null;
}