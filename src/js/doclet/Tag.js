/**
 * JavaScript Doclet
 * (c) 2005 - 2009, Juerg Lehni, http://www.scratchdisk.com
 *
 * Doclet.js is released under the MIT license
 * http://scriptographer.org/ 
 */

// A js Tag class, to define own tag lists and override tag names with special
// render handlers.

Tag = Object.extend(new function() {
	var tags = {};

	// Inject render_macro into native tags. This finds the suitable fake tag
	// based on tag names and passes on the rendering to it.
	[TagImpl, SeeTagImpl].each(function(type) {
		type.inject({
			render_macro: function() {
				var name = this.name();
				var tag = tags[name];
				if (tag) {
					// Call method from pseudo tag implementiation on native tag.
					return tag.render.apply(this, arguments);
				} else {
					// Default
					return name && name[0] == '@'
						? name.substring(1) + ' ' + this.text()
						: this.text();
				}
			}
		});
	});

	return {
		initialize: function(str) {
			this.str = str;
		},

		render_macro: function(param) {
			return this.str;
		},

		statics: {
			extend: function(src) {
				return src._names.split(',').each(function(tag) {
					tags[tag] = new this();
				}, this.base(src));
			},

			findClass: function(name, param) {
				var pkg = param.packageDoc || param.doc && param.doc.containingPackage();
				return pkg && pkg.findClass(name);
			}
		}
	}
});

LinkTag = Tag.extend({
	_names: '@link,@see',

	render: function(param) {
		var ref = this.referencedMember() || this.referencedClass();
		if (!ref) {
			// Try to find this object in the current package
			ref = Tag.findClass(this.referencedClassName(), param);
			if (this.referencedMemberName()) {
				// TODO: Search for referencedMemberName now too!
				error('ERROR: implement code to search for: ' + this.referencedMemberName());
				ref = null;
			}
		}
		if (ref) {
			// Create a new param object that has the title set to label, if defined
			var label = this.label();
			if (label)
				param = Hash.merge({ title: label }, param);
			if (!ref.isVisible()) {
				// If it's not visible, it might be a relative link that should be resolved again,
				// since it might point to a visible subclass through comment inheriting.
				if (/^#/.test(this.text())) {
					var [reference, label] = this.text().split(/\s/) || [];
				 	var mem = Member.getByReference(reference, param.doc);
					if (mem)
						return mem.renderLink(param);
				}
				error(this.position() + ': warning - ' + this.name() 
						+ ' contains reference to invisible object: ' + ref + (param.doc ? ', from ' + param.doc : ''));
			}
			return ref.renderLink(param);
		} else {
			error(this.position() + ': warning - ' + this.name()
					+ ' contains undefined reference: ' + this + (param.doc ? ', from ' + param.doc : ''));
		}
	}
});

AdditionalTag = Tag.extend({
	_names: '@additional',

	render: function(param) {
		var ref = Tag.findClass(this.text(), param);
		if (ref) {
			return ref.renderAdditional();
		} else {
			error(this.position() + ': warning - ' + this.name()
					+ ' contains undefined reference: ' + this + (param.doc ? ', from ' + param.doc : ''));
		}
	}
});

EnumTag = Tag.extend({
	_names: '@enum',

	render: function(param) {
		var ref = Tag.findClass(this.text(), param);
		if (ref) {
			return ref.renderEnumConstants();
		} else {
			error(this.position() + ': warning - ' + this.name()
					+ ' contains undefined reference: ' + this + (param.doc ? ', from ' + param.doc : ''));
		}
	}
});

GroupTag = Tag.extend({
	_names: '@grouptitle,@grouptext',

	render: function(param) {
		var name = this.name().substring(6);
		// Do not override if it's defined already, to allow @copy tags in comments
		// still define different title (or even erase them)
		if (data.group[name] === undefined)
			data.group[name] = this.text();
	}
});

BooleanTag = Tag.extend({
	_names: '@boolean,@true',

	render: function(param) {
		return '<tt>true</tt> ' + this.text() + ', <tt>false</tt> otherwise';
	}
});

CodeTag = Tag.extend({
	_names: '@code',

	render: function(param) {
		return '<tt>' + this.text() + '</tt>';
	}
});

DefaultTag = Tag.extend({
	_names: '@default',

	render: function(param) {
		var value = this.text();
		if (/^([^a-z]|true|false|null|undefined)/.test(value))
			value = '<tt>' + value + '</tt>';
		data.defaultValue = 'optional, default: ' + value;
	}
});

PackageListTag = Tag.extend({
	_names: '@packagelist',

	render: function(param) {
		return this.text();
	}
});

HeadingTag = Tag.extend({
	_names: '@heading',

	render: function(param) {
		var [all, level, str] = this.text().match(/^(\d*)\s*(.*)$/) || [];
		if (level) {
			var html = heading_filter(str, param, level);
			if (level == 2 && param.nestHeadings) {
				if (param.nestHeadings.nested) {
					html = '</ul>' + html;
					param.nestHeadings.nested = false;
				}
				html += '<ul>';
				param.nestHeadings.nested = true;
			}
			return html;
		}
	}
});

RulerTag = Tag.extend({
	_names: '@ruler',

	render: function(param) {
		return '<hr />';
	}
}); 
