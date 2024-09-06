package com.example.app.models

data class Entity(
    val species: String,                    // The name of the species
    val scientificName: String,             // The scientific name of the species
    val habitat: String,                    // The habitat of the species
    val diet: String,                       // The diet of the species
    val conservationStatus: String,         // The conservation status of the species
    val averageLifespan: Int,               // The average lifespan of the species
    val description: String                 // A brief description of the species
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
