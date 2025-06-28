package com.luulv.vn.backendtracking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // Cấu hình này cần thiết nếu sử dụng @CreatedDate và @LastModifiedDate
}
