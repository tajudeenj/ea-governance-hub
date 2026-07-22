package com.bank.ea.web;

import com.bank.ea.domain.AuditEvent;
import com.bank.ea.domain.CatalogItem;
import com.bank.ea.repo.AuditRepository;
import com.bank.ea.repo.CatalogRepository;
import com.bank.ea.repo.ProjectRepository;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EaController {
  private final CatalogRepository items;
  private final AuditRepository audits;
  private final ProjectRepository projects;

  EaController(CatalogRepository items, AuditRepository audits, ProjectRepository projects) {
    this.items = items;
    this.audits = audits;
    this.projects = projects;
  }

  @GetMapping("/login") String login() { return "login"; }

  @GetMapping("/") String dashboard(Model model) {
    Map<String, Long> counts = new LinkedHashMap<>();
    for (var type : CatalogItem.Type.values()) counts.put(type.name(), items.countByType(type));
    model.addAttribute("counts", counts);
    model.addAttribute("approved", items.countByStatus(CatalogItem.Status.APPROVED));
    model.addAttribute("deprecated", items.countByStatus(CatalogItem.Status.DEPRECATED));
    model.addAttribute("recent", audits.findTop100ByOrderByOccurredAtDesc().stream().limit(8).toList());
    return "dashboard";
  }

  @GetMapping("/catalog/{type}") String list(@PathVariable CatalogItem.Type type, Model model) {
    model.addAttribute("type", type);
    model.addAttribute("items", items.findByTypeOrderByUpdatedAtDesc(type));
    return "catalog";
  }

  @GetMapping("/catalog/{type}/new") String create(@PathVariable CatalogItem.Type type, Model model) {
    CatalogItem item = new CatalogItem(); item.setType(type);
    prepareForm(model, item);
    return "form";
  }

  @GetMapping("/catalog/{type}/{id}") String edit(@PathVariable CatalogItem.Type type,
      @PathVariable Long id, Model model) {
    CatalogItem item = items.findById(id).orElseThrow();
    if (item.getType() != type) throw new IllegalArgumentException("Record type does not match URL");
    prepareForm(model, item);
    return "form";
  }

  @PostMapping("/catalog/save") String save(@Valid @ModelAttribute CatalogItem submitted,
      BindingResult errors, Principal principal, Model model, RedirectAttributes flash) {
    if (errors.hasErrors()) {
      prepareForm(model, submitted);
      return "form";
    }

    boolean fresh = submitted.getId() == null;
    var selectedProject = submitted.getProjectId() == null ? null : projects.findById(submitted.getProjectId()).orElseThrow();
    CatalogItem record;
    if (fresh) {
      record = submitted;
      record.setProject(selectedProject);
    } else {
      record = items.findById(submitted.getId()).orElseThrow();
      record.setName(submitted.getName());
      record.setDescription(submitted.getDescription());
      record.setOwner(submitted.getOwner());
      record.setStatus(submitted.getStatus());
      record.setCriticality(submitted.getCriticality());
      record.setClassification(submitted.getClassification());
      record.setTargetDate(submitted.getTargetDate());
      record.setProject(selectedProject);
    }

    CatalogItem saved = items.save(record);
    audits.save(new AuditEvent(principal.getName(), fresh ? "CREATE" : "UPDATE",
        saved.getType().name(), saved.getId().toString(), saved.getName() + " -> " + saved.getStatus()));
    flash.addFlashAttribute("message", "Saved successfully");
    return "redirect:/catalog/" + saved.getType();
  }

  @GetMapping("/audit") String audit(Model model) {
    model.addAttribute("events", audits.findTop100ByOrderByOccurredAtDesc());
    return "audit";
  }

  private void prepareForm(Model model, CatalogItem item) {
    model.addAttribute("item", item);
    model.addAttribute("statuses", CatalogItem.Status.values());
    model.addAttribute("projects", projects.findAllByOrderByUpdatedAtDesc());
  }
}
