package fr.esupportail.esupstage.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.esupportail.esupstage.domain.jpa.Teacher;
import fr.esupportail.esupstage.domain.jpa.TeacherRepository;
import fr.esupportail.esupstage.exception.NotFoundException;
import fr.esupportail.esupstage.services.beans.TeacherBean;

@Service
public class TeacherService {

	private final TeacherRepository teacherRepository;

	@Autowired
	public TeacherService(final TeacherRepository teacherRepository) {
		super();
		this.teacherRepository = teacherRepository;
	}

	public Page<TeacherBean> findAll(final Pageable pageable) {
		return this.teacherRepository.findAll(pageable).map(TeacherService::convert);
	}

	public TeacherBean findById(final String email) {
		return this.teacherRepository.findById(email).map(TeacherService::convert)
				.orElseThrow(NotFoundException::new);
	}

	public TeacherBean save(final TeacherBean bean) {
		return TeacherService.convert(this.teacherRepository.save(TeacherService.convert(bean)));
	}

	public void deleteById(final String email) {
		this.teacherRepository.deleteById(email);
	}

	public static TeacherBean convert(final Teacher feed) {
		final TeacherBean result = new TeacherBean();
		BeanUtils.copyProperties(feed, result);
		return result;
	}

	public static Teacher convert(final TeacherBean feed) {
		final Teacher result = new Teacher();
		BeanUtils.copyProperties(feed, result);
		return result;
	}

}
