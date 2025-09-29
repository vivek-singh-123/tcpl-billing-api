package com.tcpl.billing.service;

import com.tcpl.billing.dto.ProjectDashboardResponse;
import com.tcpl.billing.model.Invoice;
import com.tcpl.billing.model.Project;
import com.tcpl.billing.repository.InvoiceRepository;
import com.tcpl.billing.repository.PaymentRepository;
import com.tcpl.billing.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;

    // --- 1. List projects with optional filters ---
    public List<Project> listProjects(String status, Long companyId, LocalDate start, LocalDate end) {
        if (status != null && companyId != null && start != null && end != null) {
            return projectRepo.findByStatusAndCompanyIdAndStartDateBetween(status, companyId, start, end);
        } else if (status != null && companyId != null) {
            return projectRepo.findByStatusAndCompanyId(status, companyId);
        } else if (companyId != null) {
            return projectRepo.findByCompanyId(companyId);
        } else if (status != null) {
            return projectRepo.findByStatus(status);
        } else {
            return projectRepo.findAll();
        }
    }

    // --- 2. Create project ---
    public Project createProject(Project project) {
        return projectRepo.save(project);
    }

    // --- 3. Get project details ---
    public Project getProject(Long id) {
        return projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // --- 4. Update project ---
    public Project updateProject(Long id, Project updated) {
        Project existing = getProject(id);
        existing.setName(updated.getName());
        existing.setCompanyId(updated.getCompanyId());
        existing.setStatus(updated.getStatus());
        existing.setBudget(updated.getBudget());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        return projectRepo.save(existing);
    }

    // --- 5. Get dashboard KPIs ---
    public ProjectDashboardResponse getProjectDashboard(Long projectId) {
        Project project = getProject(projectId);

        // Fetch invoices for this project
        List<Invoice> invoices = invoiceRepo.findByProjectId(projectId);

        // Calculate total spent (sum of totalPaid)
        double spent = invoices.stream()
                .mapToDouble(Invoice::getTotalPaid)
                .sum();

        // Count invoices and payments
        int invoicesCount = invoices.size();
        int paymentsCount = invoices.stream()
                .flatMap(inv -> inv.getPayments().stream())
                .mapToInt(p -> 1)
                .sum();

        // Progress percentage capped at 100%
        int progressPercent = project.getBudget() > 0
                ? (int) Math.min((spent / project.getBudget()) * 100, 100)
                : 0;

        return new ProjectDashboardResponse(
                project.getId(),
                project.getName(),
                project.getBudget(),
                spent,
                progressPercent,
                invoicesCount,
                paymentsCount
        );
    }
}
