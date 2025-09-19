package com.bar.gestiondesfichier.controller;

import com.bar.gestiondesfichier.entity.FileEntity;
import com.bar.gestiondesfichier.entity.Account;
import com.bar.gestiondesfichier.repository.FileRepository;
import com.bar.gestiondesfichier.repository.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
@Tag(name = "user", description = "DEPRECATED - Use /api/documents instead")
@SecurityRequirement(name = "bearerAuth")
@Deprecated
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    @Operation(summary = "Get user files", description = "Get all files for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<FileEntity>> getUserFiles(Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<Account> accountOpt = accountRepository.findByUsername(username);
            
            if (accountOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Account account = accountOpt.get();
            List<FileEntity> files = fileRepository.findByOwnerAndActiveTrue(account);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get file by ID", description = "Get a specific file by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "File not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FileEntity> getFileById(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<Account> accountOpt = accountRepository.findByUsername(username);
            
            if (accountOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Account account = accountOpt.get();
            Optional<FileEntity> fileOpt = fileRepository.findById(id);
            
            if (fileOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            FileEntity file = fileOpt.get();
            
            // Check if user owns the file or is admin
            if (!file.getOwner().getId().equals(account.getId()) && 
                !"ADMIN".equals(account.getAccountCategory().getName())) {
                return ResponseEntity.status(403).build();
            }
            
            return ResponseEntity.ok(file);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Get file statistics", description = "Get file statistics for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Map<String, Object>> getFileStats(Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<Account> accountOpt = accountRepository.findByUsername(username);
            
            if (accountOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Account account = accountOpt.get();
            long fileCount = fileRepository.countByOwnerAndActiveTrue(account);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFiles", fileCount);
            stats.put("owner", account.getFullName());
            stats.put("username", account.getUsername());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}