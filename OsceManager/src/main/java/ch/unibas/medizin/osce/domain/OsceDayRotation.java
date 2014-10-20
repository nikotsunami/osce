package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.shared.BreakType;

@RooJavaBean
@RooToString
@RooEntity
public class OsceDayRotation {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(OsceDayRotation.class);
	
	private Integer rotationNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeEnd;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@ManyToOne
	private OsceSequence osceSequence;
	
	private BreakType breakType;
	
	private Integer breakDuration;
	
	@Transactional
	public void deleteOsceRotationDataByOsceDay(Long osceDayId)
	{
		String sql = "delete from osce_day_rotation where id > 0 and osce_day = " + osceDayId;
		int deletedCount = entityManager().createNativeQuery(sql).executeUpdate();
	}
}
