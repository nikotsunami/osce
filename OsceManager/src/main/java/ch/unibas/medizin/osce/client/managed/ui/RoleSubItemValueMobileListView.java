package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldMobileApp;
import ch.unibas.medizin.osce.client.scaffold.ui.MobileProxyListView;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.client.DateTimeFormatRenderer;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.Renderer;
import java.util.HashSet;
import java.util.Set;

public class RoleSubItemValueMobileListView extends RoleSubItemValueMobileListView_Roo_Gwt {

    private static ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueMobileListView instance;

    public RoleSubItemValueMobileListView() {
        super("New RoleSubItemValue", new CellRenderer());
        init();
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoleSubItemValueMobileListView instance() {
        if (instance == null) {
            instance = new RoleSubItemValueMobileListView();
        }
        return instance;
    }

    public String[] getPaths() {
        return paths.toArray(new String[paths.size()]);
    }

    private static class CellRenderer extends AbstractSafeHtmlRenderer<RoleSubItemValueProxy> {

        private final String dateStyle = ScaffoldMobileApp.getMobileListResources().cellListStyle().dateProp();

        private final String secondaryStyle = ScaffoldMobileApp.getMobileListResources().cellListStyle().secondaryProp();

        private final Renderer<String> primaryRenderer = new AbstractRenderer<String>() {

            public String render(java.lang.String obj) {
                return obj == null ? "" : String.valueOf(obj);
            }
        };

        private final Renderer<Integer> secondaryRenderer = new AbstractRenderer<Integer>() {

            public String render(java.lang.Integer obj) {
                return obj == null ? "" : String.valueOf(obj);
            }
        };

        @Override
        public SafeHtml render(RoleSubItemValueProxy value) {
            if (value == null) {
                return SafeHtmlUtils.EMPTY_SAFE_HTML;
            }
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            if (value.getItemText() != null) {
                sb.appendEscaped(primaryRenderer.render(value.getItemText()));
            }
            sb.appendHtmlConstant("<div style=\"position:relative;\">");
            sb.appendHtmlConstant("<div class=\"" + secondaryStyle + "\">");
            if (value.getId() != null) {
                sb.appendEscaped(secondaryRenderer.render(value.getId()));
            }
            sb.appendHtmlConstant("</div>");
            sb.appendHtmlConstant("<div class=\"" + dateStyle + "\">");
            sb.appendHtmlConstant("</div>");
            sb.appendHtmlConstant("</div>");
            return sb.toSafeHtml();
        }
    }
}
