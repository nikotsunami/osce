package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.domain.Skill;
import ch.unibas.medizin.osce.domain.SkillLevel;
import ch.unibas.medizin.osce.domain.Topic;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Skill.class)
public interface SkillRequestNonRoo extends RequestContext {
	abstract Request<List<SkillProxy>> findSkillByTopic(TopicProxy val);
	
	abstract Request<List<SkillProxy>> findSkillByTopicIDAndSkillLevelID(long topicId, long skillLevelId);
}
