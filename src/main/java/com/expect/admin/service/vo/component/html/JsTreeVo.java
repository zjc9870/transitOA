package com.expect.admin.service.vo.component.html;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.service.vo.component.BaseVo;

public class JsTreeVo extends BaseVo implements Comparable<JsTreeVo> {

	private String id;
	private String text;
	private State state = new State();
	// js的图标,支持font-awesome
	private String icon;
	private int sequence;
	private List<JsTreeVo> children = new ArrayList<JsTreeVo>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public State getState() {
		return state;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public List<JsTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<JsTreeVo> children) {
		this.children = children;
	}

	public void addChildren(JsTreeVo child) {
		this.children.add(child);
	}

	public void setState(boolean selected, boolean opened, boolean diabled) {
		this.state.selected = selected;
		this.state.opened = opened;
		this.state.disabled = diabled;
	}

	public void setSelected(boolean selected) {
		this.state.selected = selected;
	}

	@Override
	public int compareTo(JsTreeVo o) {
		if (this.sequence > o.getSequence()) {
			return 1;
		} else if (this.sequence < o.getSequence()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "JsTreeVo [id=" + id + ", text=" + text + ", state=" + state + ", icon=" + icon + ", sequence="
				+ sequence + ", children=" + children + "]";
	}

	// tree状态，包括是否选择，是否打开,是否可用,默认都是false
	class State {

		private boolean selected = false;
		private boolean opened = false;
		private boolean disabled = false;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isOpened() {
			return opened;
		}

		public void setOpened(boolean opened) {
			this.opened = opened;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

		@Override
		public String toString() {
			return "State [selected=" + selected + ", opened=" + opened + ", disabled=" + disabled + "]";
		}

	}

}
