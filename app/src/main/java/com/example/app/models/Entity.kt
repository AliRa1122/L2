package com.example.app.models

data class Entity(
    val species: String,
    val scientificName: String,
    val habitat: String,
    val diet: String,
    val conservationStatus: String,
    val averageLifespan: Int,
    val description: String
)

/*  Example Data
{
    "species": "African Elephant",
    "scientificName": "Loxodonta africana",
    "habitat": "Savanna",
    "diet": "Herbivore",
    "conservationStatus": "Vulnerable",
    "averageLifespan": 60,
    "description": "The largest land animal, known for its intelligence, social behavior, and distinctive trunk."
}
*/
