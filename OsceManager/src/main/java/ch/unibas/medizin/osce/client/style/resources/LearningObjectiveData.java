package ch.unibas.medizin.osce.client.style.resources;

import ch.unibas.medizin.osce.client.managed.request.SkillProxy;

import com.google.gwt.user.client.ui.CheckBox;

public class LearningObjectiveData {
	
	public SkillProxy skill;
	
	public String code;
	
	public String text;
	
	public String topic;
	
	public String skillLevel;
	
	public String d;
	
	public String t;
	
	public String e;
	
	public String p;
	
	public String g;
	
	public SkillProxy getSkill() {
		return skill;
	}

	public void setSkill(SkillProxy skill) {
		this.skill = skill;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}
}
