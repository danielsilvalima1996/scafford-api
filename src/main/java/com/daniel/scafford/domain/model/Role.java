package com.daniel.scafford.domain.model;

import java.util.Set;

public record Role(String name, boolean isEnabled, Set<Permission> permissions) {
}