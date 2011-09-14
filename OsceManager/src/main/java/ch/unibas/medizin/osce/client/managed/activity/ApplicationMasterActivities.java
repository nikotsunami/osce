package ch.unibas.medizin.osce.client.managed.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationEntityTypesProcessor;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicListView;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.RoomListView;
import ch.unibas.medizin.osce.client.managed.ui.RoomMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.ScarListView;
import ch.unibas.medizin.osce.client.managed.ui.ScarMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterListView;
import ch.unibas.medizin.osce.client.managed.ui.SemesterMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageListView;
import ch.unibas.medizin.osce.client.managed.ui.SpokenLanguageMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleListView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleMobileListView;
import ch.unibas.medizin.osce.client.managed.ui.StudentListView;
import ch.unibas.medizin.osce.client.managed.ui.StudentMobileListView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public final class ApplicationMasterActivities extends ApplicationMasterActivities_Roo_Gwt {

    @Inject
    public ApplicationMasterActivities(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }
}
