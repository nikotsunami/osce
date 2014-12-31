package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;

public class StandardizedRoleProxyRenderer extends ProxyRenderer<StandardizedRoleProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleProxyRenderer INSTANCE;

    protected StandardizedRoleProxyRenderer() {
        super(new String[] { "shortName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.StandardizedRoleProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new StandardizedRoleProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(StandardizedRoleProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortName() + " (" + object.getId() + ")";
    }
}
