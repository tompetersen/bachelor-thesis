package jProtocol.Abstract.View.keyvaluetree;

import java.awt.Color;
import java.util.List;

/**
 * A KeyValueObject has a key, an optional value and optional children and can be 
 * displayed in a KeyValueTree. A background color and a HTML info text can be set 
 * additionally.
 * 
 * @author Tom Petersen, 2015
 */
public class KeyValueObject {
	private String _key;
	private String _value;
	private Color _backgroundColor;
	private List<KeyValueObject> _kvoList;
	private String _htmlInfoContent;

	/**
	 * Creates a key value object with key and value.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public KeyValueObject(String key, String value) {
		super();
		this._key = key;
		this._value = value;
	}

	/**
	 * Creates a key value object with a key and a list of children
	 * 
	 * @param key the key
	 * @param childList the children of the object
	 */
	public KeyValueObject(String key, List<KeyValueObject> childList) {
		this._key = key;
		this._kvoList = childList;
	}
	
	public String getKey() {
		return _key;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		_value = value;
	}

	public List<KeyValueObject> getChildList() {
		return _kvoList;
	}
	
	public boolean hasChildren() {
		return (_kvoList != null && _kvoList.size() > 0);
	}
	
	public Color getBackgroundColor() {
		return _backgroundColor;
	}
	
	public void setBackgroundColor(Color c) {
		this._backgroundColor = c;
	}

	public String getHtmlInfoContent() {
		return _htmlInfoContent;
	}

	public void setHtmlInfoContent(String htmlHelpContent) {
		_htmlInfoContent = htmlHelpContent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
		result = prime * result + ((_kvoList == null) ? 0 : _kvoList.hashCode());
		result = prime * result + ((_value == null) ? 0 : _value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KeyValueObject))
			return false;
		KeyValueObject other = (KeyValueObject) obj;
		
		if (!_key.equals(other._key))
			return false;
		
		if (_kvoList == null) {
			if (other._kvoList != null)
				return false;
		}
		else if (!_kvoList.equals(other._kvoList))
			return false;
		
		if (_value == null) {
			if (other._value != null)
				return false;
		}
		else if (!_value.equals(other._value))
			return false;
		
		return true;
	}
}