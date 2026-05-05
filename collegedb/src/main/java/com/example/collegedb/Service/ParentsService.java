package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.ParentsRepository;
import com.example.collegedb.Response.ParentsResponse;
import com.example.collegedb.entity.Parents;

@Service
@Transactional
public class ParentsService {

    private final ParentsRepository parentsRepository;

    public ParentsService(ParentsRepository parentsRepository) {
        this.parentsRepository = parentsRepository;
    }

    @Transactional(readOnly = true)
    public ParentsResponse getParentsById(Long id) {
        return toResponse(getExistingParent(id));
    }

    @Transactional(readOnly = true)
    public List<ParentsResponse> getAllParents() {
        return parentsRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ParentsResponse createParent(Parents parent) {
        return toResponse(parentsRepository.save(parent));
    }

    public ParentsResponse updateParent(Long id, Parents updatedParent) {
        Parents existingParent = getExistingParent(id);
        existingParent.setParentName(updatedParent.getParentName());
        existingParent.setEmail(updatedParent.getEmail());
        existingParent.setContactNumber(updatedParent.getContactNumber());
        existingParent.setRelationship(updatedParent.getRelationship());
        existingParent.setStudent(updatedParent.getStudent());
        existingParent.setUser(updatedParent.getUser());
        return toResponse(parentsRepository.save(existingParent));
    }

    public void deleteParent(Long id) {
        Parents parent = getExistingParent(id);
        parentsRepository.delete(parent);
    }

    public ParentsResponse patchParent(Long id, Parents updatedFields) {
        Parents existingParent = getExistingParent(id);
        if (updatedFields.getParentName() != null) {
            existingParent.setParentName(updatedFields.getParentName());
        }
        if (updatedFields.getEmail() != null) {
            existingParent.setEmail(updatedFields.getEmail());
        }
        if (updatedFields.getContactNumber() != null) {
            existingParent.setContactNumber(updatedFields.getContactNumber());
        }
        if (updatedFields.getRelationship() != null) {
            existingParent.setRelationship(updatedFields.getRelationship());
        }
        if (updatedFields.getStudent() != null) {
            existingParent.setStudent(updatedFields.getStudent());
        }
        if (updatedFields.getUser() != null) {
            existingParent.setUser(updatedFields.getUser());
        }
        return toResponse(parentsRepository.save(existingParent));
    }

    private Parents getExistingParent(Long id) {
        return parentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parent not found with ID: " + id));
    }

    private ParentsResponse toResponse(Parents parent) {
        return new ParentsResponse(
            parent.getParentId(),
            parent.getParentName(),
            parent.getEmail(),
            parent.getContactNumber(),
            parent.getRelationship(),
            parent.getStudent() != null ? parent.getStudent().getStudentId() : null,
            parent.getUser() != null ? parent.getUser().getUserId() : null
        );
    }
}
