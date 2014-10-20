package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisChecksValueProxy;


public interface SPDetailsReviewAnamnesisTableSubView {
	public interface Delegate {
	}
	
	public void setDelegate(Delegate delegate);

	public boolean setValue(List<AnamnesisChecksValueProxy> response1,List<SpAnamnesisChecksValueProxy> response2);
}
