package com.example.AplicatieMeditatii.customer;

public record CustomerUpdateRequest (
        String name,
        String email,
        Integer age
) {
}
