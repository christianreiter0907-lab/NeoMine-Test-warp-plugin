package de.neomine.warp;

import java.util.UUID;

public record Warp(
    String name,
    String world,
    double x,
    double y,
    double z,
    float yaw,
    float pitch,
    UUID creator,
    long date
) {}