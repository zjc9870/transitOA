var Tools = function() {
	
	var getUUID = function() {
		var len = 32; // 32长度
		var radix = 16; // 16进制
		var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
				.split('');
		var uuid = [], i;
		radix = radix || chars.length;
		if (len) {
			for (i = 0; i < len; i++)
				uuid[i] = chars[0 | Math.random() * radix];
		} else {
			var r;
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			uuid[14] = '4';
			for (i = 0; i < 36; i++) {
				if (!uuid[i]) {
					r = 0 | Math.random() * 16;
					uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
				}
			}
		}
		return uuid.join('');
	};

	var replaceAll = function(str,s1, s2) {
		return str.replace(new RegExp(s1, "gm"), s2);
	};

	var indexOf = function(array) {
		for (var i = 0; i < array.length; i++) {
			if (array[i] == val)
				return i;
		}
		return -1;
	};

	var remove = function(array) {
		var index = indexOf(val);
		if (index > -1) {
			array.splice(index, 1);
		}
	};

	return {
		getUUID : function() {
			return getUUID();
		},
		replaceAll : function(str,s1, s2) {
			return replaceAll(str,s1, s2);
		},
		remove : function(array) {
			remove(array);
		}
	}
}();
