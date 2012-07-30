package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Comparator;

import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;

public class SequenceComparator implements Comparator<OsceSequenceProxy>{

	@Override
	public int compare(OsceSequenceProxy o1, OsceSequenceProxy o2) {
		// TODO Auto-generated method stub
		return (o1.getId()>o2.getId() ? 1 : (o1.getId()==o2.getId() ? 0 : -1));
	}

}
