package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.domain.Skill;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Skill.class)
public interface SkillRequestNonRoo extends RequestContext {
	//abstract Request<List<SkillProxy>> findSkillByTopic(TopicProxy val);
	
		abstract Request<List<SkillProxy>> findSkillByTopicIDAndSkillLevelID(long topicId, long skillLevelId);
		
		abstract Request<List<SkillProxy>> findAllSkillByLimit(int start, int length);
		
		abstract Request<List<SkillProxy>> findSkillBySearchCriteria
				(int start, int max, Long mainClassificationId, Long classificationTopicId, Long topidId, Long skillLevlId, Long applianceId);
		
		abstract Request<Integer> countSkillBySearchCriteria
				(Long mainClassificationId, Long classificationTopicId, Long topidId, Long skillLevlId, Long applianceId);
}
