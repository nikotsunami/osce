package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImportObjectiveView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImportObjectiveViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.client.style.resources.LearningObjectiveData;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.domain.SkillHasAppliance;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ImportObjectiveViewActivity extends AbstractActivity implements ImportObjectiveView.Presenter, ImportObjectiveView.Delegate, LearningObjectiveView.Delegate, LearningObjectiveView.Presenter {

	 private OsMaRequestFactory requests;
	 private PlaceController placeController;
	 private AcceptsOneWidget widget;
	 private ImportObjectiveView view; 
	 private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();
	 private LearningObjectiveData learningObjective;
	 List<SkillProxy> skillList = new ArrayList<SkillProxy>();
	 String temp = "";
	 List<SkillHasApplianceProxy> skillHasApplianceProxies = new ArrayList<SkillHasApplianceProxy>();

	 private Long mainClassificationId = null;
	 private Long classificaitonTopicId = null;
	 private Long topicId = null;
	 private Long skillLevelId = null;
	 private Long applianceId = null;
	 
	 private LearningObjectiveView learningObjectiveView;
	
	public ImportObjectiveViewActivity(OsMaRequestFactory requests, PlaceController placeController)
	{
		this.requests = requests;
    	this.placeController = placeController;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		Log.info("SystemStartActivity.start()");
		ImportObjectiveView systemStartView = new ImportObjectiveViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		learningObjectiveView = view.getLearningObjectiveViewImpl();
		learningObjectiveView.setDelegate(this);
		
		RecordChangeEvent.register(requests.getEventBus(), (LearningObjectiveViewImpl) learningObjectiveView);
		
		MenuClickEvent.register(requests.getEventBus(), (ImportObjectiveViewImpl) view);
	
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						//Log.info("~~~~~~~~ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
						
					}
		});
		
		view.getLearningObjectiveViewImpl().getTable().addRangeChangeHandler(new RangeChangeEvent.Handler(){

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onRangeChanged();
			}
			
		});
		
		view.getLearningObjectiveViewImpl().getLearningScrollPanel().addStyleName("learningObjTable");
		
		init();
		
		init2();
		
		view.setDelegate(this);
	}

	@Override
	public void goTo(Place place) {
	}
	
	public void fillMainClassificationSuggestBox()
	{
		requests.mainClassificationRequest().findAllMainClassifications().fire(new OSCEReceiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				DefaultSuggestOracle<MainClassificationProxy> suggestOracle1 = (DefaultSuggestOracle<MainClassificationProxy>) view.getLearningObjectiveViewImpl().getMainClassificationSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(response);
				view.getLearningObjectiveViewImpl().getMainClassificationSuggestBox().setSuggestOracle(suggestOracle1);
				
				view.getLearningObjectiveViewImpl().getMainClassificationSuggestBox().setRenderer(new AbstractRenderer<MainClassificationProxy>() {

					@Override
					public String render(MainClassificationProxy object) {
						// TODO Auto-generated method stub
						if (object != null)
							return (object.getDescription() +  "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
				
				
			}
		});
	}
	
	public void fillClassificationTopicSuggestBox(Long mainClassiId)
	{
		
		requests.classificationTopicRequestNonRoo().findClassiTopicByMainClassi(mainClassiId).fire(new OSCEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				
				DefaultSuggestOracle<ClassificationTopicProxy> suggestOracle = (DefaultSuggestOracle<ClassificationTopicProxy>) view.getLearningObjectiveViewImpl().getClassificationTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				view.getLearningObjectiveViewImpl().getClassificationTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				view.getLearningObjectiveViewImpl().getClassificationTopicSuggestBox().setRenderer(new AbstractRenderer<ClassificationTopicProxy>() {

					@Override
					public String render(ClassificationTopicProxy object) {
						if (object != null)
							return (object.getDescription() + "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
				
			}
		});
	}
	
	public void fillTopicSuggestBox(Long classiTopicId)
	{
		requests.topicRequestNonRoo().findTopicByClassiTopic(classiTopicId).fire(new OSCEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
							
				DefaultSuggestOracle<TopicProxy> suggestOracle = (DefaultSuggestOracle<TopicProxy>) view.getLearningObjectiveViewImpl().getTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				view.getLearningObjectiveViewImpl().getTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				view.getLearningObjectiveViewImpl().getTopicSuggestBox().setRenderer(new AbstractRenderer<TopicProxy>() {

					@Override
					public String render(TopicProxy object) {
						if (object != null)
							return object.getTopicDesc();
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillSkillLevelSuggestBox()
	{
		requests.skillLevelRequest().findAllSkillLevels().fire(new OSCEReceiver<List<SkillLevelProxy>>() {

			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
				DefaultSuggestOracle<SkillLevelProxy> suggestOracle = (DefaultSuggestOracle<SkillLevelProxy>) view.getLearningObjectiveViewImpl().getSkillLevelSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				view.getLearningObjectiveViewImpl().getSkillLevelSuggestBox().setSuggestOracle(suggestOracle);
				
				view.getLearningObjectiveViewImpl().getSkillLevelSuggestBox().setRenderer(new AbstractRenderer<SkillLevelProxy>() {

					@Override
					public String render(SkillLevelProxy object) {
						if (object != null)
							return String.valueOf(object.getLevelNumber());
						else
							return "";
					}
				});
			}
		});
	}

	public void fillApplianceSuggestBox()
	{
		requests.applianceRequest().findAllAppliances().fire(new OSCEReceiver<List<ApplianceProxy>>() {

			@Override
			public void onSuccess(List<ApplianceProxy> response) {
				DefaultSuggestOracle<ApplianceProxy> suggestOracle = (DefaultSuggestOracle<ApplianceProxy>) view.getLearningObjectiveViewImpl().getApplianceSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				view.getLearningObjectiveViewImpl().getApplianceSuggestBox().setSuggestOracle(suggestOracle);
				
				view.getLearningObjectiveViewImpl().getApplianceSuggestBox().setRenderer(new AbstractRenderer<ApplianceProxy>() {

					@Override
					public String render(ApplianceProxy object) {
						if (object != null)
							return object.getShortcut();
						else
							return "";
					}
				});			
			}
		});
	}
	
	public void init()
	{ 
		Range range = view.getLearningObjectiveViewImpl().getTable().getVisibleRange();
		int start = range.getStart();
		int length = range.getLength();
		
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
		
		requests.skillRequestNonRoo().countSkillBySearchCriteria(mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).fire(new OSCEReceiver<Integer>() {
			@Override
			public void onSuccess(Integer response) {
				view.getLearningObjectiveViewImpl().getTable().setRowCount(response);
			}
		});
		
		requests.skillRequestNonRoo().findSkillBySearchCriteria(start, length, mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<SkillProxy>>() {

			@Override
			public void onSuccess(List<SkillProxy> response) {
				
				for (int i=0; i<response.size(); i++)
				{
					learningObjective = new LearningObjectiveData();
					SkillProxy skill = response.get(i);
					
					temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
					learningObjective.setCode(temp);
					learningObjective.setSkill(skill);
					learningObjective.setText(skill.getDescription());
					learningObjective.setTopic(skill.getTopic().getTopicDesc());	
					
					if (skill.getSkillLevel() != null)
						learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
					else
						learningObjective.setSkillLevel("");
					
					Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
					
					while (iter.hasNext())
					{
						SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
						
						if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
							learningObjective.setD("D");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
							learningObjective.setT("T");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
							learningObjective.setE("E");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
							learningObjective.setP("P");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
							learningObjective.setG("G");
					}
					
					learningObjectiveData.add(learningObjective);
					learningObjective = null;
				}
				
				view.getLearningObjectiveViewImpl().getTable().setRowData(learningObjectiveData);
				view.getLearningObjectiveViewImpl().getTable().setVisibleRange(0, OsMaConstant.TABLE_PAGE_SIZE);
				requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			}
		});
		
		
	}
	
	public void init2()
	{
		fillMainClassificationSuggestBox();
		
		fillClassificationTopicSuggestBox(mainClassificationId);
		
		fillTopicSuggestBox(classificaitonTopicId);
		
		fillSkillLevelSuggestBox();
		
		fillApplianceSuggestBox();
	}

	protected void onRangeChanged() {
		learningObjectiveData.clear();		
		final Range range = view.getLearningObjectiveViewImpl().getTable().getVisibleRange();
		
		requests.skillRequestNonRoo().countSkillBySearchCriteria(mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).fire(new OSCEReceiver<Integer>() {
			@Override
			public void onSuccess(Integer response) {
				view.getLearningObjectiveViewImpl().getTable().setRowCount(response);
			}
		});
		
		requests.skillRequestNonRoo().findSkillBySearchCriteria(range.getStart(), range.getLength(), mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<SkillProxy>>() {

			@Override
			public void onSuccess(List<SkillProxy> response) {
			
				for (int i=0; i<response.size(); i++)
				{
					learningObjective = new LearningObjectiveData();
					SkillProxy skill = response.get(i);
					
					temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
					learningObjective.setCode(temp);
					learningObjective.setSkill(skill);
					learningObjective.setText(skill.getDescription());
					learningObjective.setTopic(skill.getTopic().getTopicDesc());	
					
					if (skill.getSkillLevel() != null)
						learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
					else
						learningObjective.setSkillLevel("");
					
					Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
					
					while (iter.hasNext())
					{
						SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
						
						if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
							learningObjective.setD("D");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
							learningObjective.setT("T");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
							learningObjective.setE("E");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
							learningObjective.setP("P");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
							learningObjective.setG("G");
					}
					
					learningObjectiveData.add(learningObjective);
				}
				
				view.getLearningObjectiveViewImpl().getTable().setRowData(range.getStart(), learningObjectiveData);
			}
		});
			
	}

	@Override
	public void mainClassificationSuggestboxChanged(Long value) {
		mainClassificationId = value;
		
		view.getLearningObjectiveViewImpl().getClassificationTopicSuggestBox().setSelected(null);
		classificaitonTopicId = null;
					
		fillClassificationTopicSuggestBox(mainClassificationId);
		onRangeChanged();
	}

	@Override
	public void classificationTopicSuggestboxChanged(Long value) {
		classificaitonTopicId = value;
		
		view.getLearningObjectiveViewImpl().getTopicSuggestBox().setSelected(null);
		topicId = null;
	
		fillTopicSuggestBox(classificaitonTopicId);
		onRangeChanged();
	}

	@Override
	public void topicSuggestboxChanged(Long value) {
		topicId = value;
		onRangeChanged();
	}

	@Override
	public void skillLevelSuggestboxChanged(Long value) {
		skillLevelId = value;
		onRangeChanged();
	}

	@Override
	public void applianceSuggestboxChanged(Long value) {
		applianceId = value;
		onRangeChanged();
	}

	@Override
	public void displayLoadingScreen(boolean value) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(value));
	}
	
	public void refreshLearningObjData()
	{
		init();
		init2();
	}
}
