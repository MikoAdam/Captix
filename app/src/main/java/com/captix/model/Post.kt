package com.captix.model

data class Post(
    val username: String,
    val id: String,
    val imageUrl: String,
    val caption: String,
    val upvote: Int,
    val downvote: Int,
    val dateOfCreation: String,
    val categoryName: String
)

/*
{"_id":"5ea6abce16fe174d921e0fb2",
    "username":"asd",
    "imageUrl":"https://captix.s3.eu-central-1.amazonaws.com/5ea18956d3c2f37d05894291.jpg",
    "caption":"Abstract image",

    "comments":[{"_id":"5ea6abce16fe174d921e0fb1",
    "user_id":"5e7a3668618a4838ebf091e3",
    "content":"Nice job!"}],

    "rating":0,
    "dateOfCreation":"2020-04-27T09:54:22.225",
    "categoryName":"Landscape"}*/
