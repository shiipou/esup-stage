package fr.esupportail.esupstage.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.esupportail.esupstage.domain.jpa.Enseignant;
import fr.esupportail.esupstage.domain.jpa.EnseignantRepository;
import fr.esupportail.esupstage.exception.NotFoundException;
import fr.esupportail.esupstage.services.beans.TeacherBean;

@Service
public class TeacherService {

	private final EnseignantRepository enseignantRepository;

	@Autowired
	public TeacherService(final EnseignantRepository enseignantRepository) {
		super();
		this.enseignantRepository = enseignantRepository;
	}

	public Page<TeacherBean> findAll(final Pageable pageable) {
		return this.enseignantRepository.findAll(pageable).map(TeacherService::convert);
	}

	public TeacherBean findById(final String email) {
		return this.enseignantRepository.findById(email).map(TeacherService::convert)
				.orElseThrow(NotFoundException::new);
	}

	public TeacherBean save(final TeacherBean bean) {
		return TeacherService.convert(this.enseignantRepository.save(TeacherService.convert(bean)));
	}

	public void deleteBy(final String email) {
		this.enseignantRepository.deleteById(email);
	}

	public static TeacherBean convert(final Enseignant feed) {
		final TeacherBean result = new TeacherBean();
		BeanUtils.copyProperties(feed, result);
		return result;
	}

	public static Enseignant convert(final TeacherBean feed) {
		final Enseignant result = new Enseignant();
		BeanUtils.copyProperties(feed, result);
		return result;
	}

}
