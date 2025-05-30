package com.devtalks.demo.ai.chat.repository;

import org.springframework.data.annotation.Id;

public record Wiki(@Id int id,
                   String feature,
                   String owner,
                   String description) {
}
