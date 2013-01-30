package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.domain.ClassificationTopic;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ClassificationTopic.class)
public interface ClassificationTopicRequestNonRoo extends RequestContext {
	abstract Request<List<ClassificationTopicProxy>> findClassiTopicByMainClassi(Long value);
}
