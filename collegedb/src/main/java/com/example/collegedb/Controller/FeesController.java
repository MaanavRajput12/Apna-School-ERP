package com.example.collegedb.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.FeesResponse;
import com.example.collegedb.Service.FeesService;
import com.example.collegedb.entity.Fees;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fees")
public class FeesController {

    private static final Logger logger = LoggerFactory.getLogger(FeesController.class);
    private final FeesService feesService;

    public FeesController(FeesService feesService) {
        this.feesService = feesService;
    }

    @GetMapping
    public List<FeesResponse> getAll() {
        logger.info("Fetching all fees records...");
        return feesService.getAllFees();
    }

    @PostMapping
    public FeesResponse create(@Valid @RequestBody Fees fees) {
        logger.info("Creating new fees record with amount: {}", fees.getAmount());
        return feesService.createFees(fees);
    }

    @PutMapping("{id}")
    public FeesResponse update(@PathVariable Long id, @RequestBody Fees updatedFees) {
        logger.info("Updating fees record with ID: {}", id);
        return feesService.updateFees(id, updatedFees);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete fees record with ID: {}", id);
        feesService.deleteFees(id);
    }

    @PatchMapping("{id}")
    public FeesResponse patch(@PathVariable Long id, @RequestBody Fees updatedFields) {
        logger.info("Patching fees record with ID: {}", id);
        return feesService.patchFees(id, updatedFields);
    }
}
