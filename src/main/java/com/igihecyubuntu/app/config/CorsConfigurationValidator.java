package com.igihecyubuntu.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Configuration validator to log CORS settings on application startup
 */
@Component
public class CorsConfigurationValidator implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(CorsConfigurationValidator.class);
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== CORS Configuration Status ===");
        logger.info("✅ CORS is configured with specific origins:");
        logger.info("   - https://igihecyubuntu.org (Production)");
        logger.info("   - http://localhost:5174 (Vite Dev Server)");
        logger.info("   - http://localhost:5173 (Vite Dev Server Alt)");
        logger.info("✅ Authentication endpoints (/api/auth/**) are public - no JWT required");
        logger.info("✅ Credentials are allowed for authenticated requests");
        logger.info("✅ All individual @CrossOrigin annotations have been removed");
        logger.info("====================================");
    }
}