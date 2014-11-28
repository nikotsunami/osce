package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.NoteType;

@RooJavaBean
@RooToString
@RooEntity
public class Notes {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	private Student student;
	
	@ManyToOne
	private Doctor doctor;

	@ManyToOne
	private OscePostRoom oscePostRoom;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@Size(max=5000)
	private String comment;
		
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastviewed;
	
	private NoteType noteType;

	public static List<Notes> findNotesByExaminerAndStudentAndNotetype(Long examinerId, Long studentId, Integer studentAudio, Long roleId) {
		EntityManager em = entityManager();
		String sql = "SELECT n FROM Notes as n  WHERE n.doctor = " + examinerId + "  and n.student.id = "+ studentId  +"  and n.noteType =" + studentAudio + "AND n.oscePostRoom IS NOT NULL AND n.oscePostRoom.oscePost.standardizedRole.id = " + roleId;
		TypedQuery<Notes> query = em.createQuery(sql, Notes.class);
		return query.getResultList();
	}
}
