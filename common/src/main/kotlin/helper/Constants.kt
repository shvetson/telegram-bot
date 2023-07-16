package ru.shvets.telegram.bot.common.helper

import kotlinx.datetime.Instant

// info to help
const val HELP_TEXT = "This bot is created to demo.\nYour can execute commands from main menu on the left."
const val SORRY_TEXT = "Sorry, this command was not recognized"

const val QUESTION_TEXT = "Do you really want to register?"
const val YES_BUTTON = "YES_BUTTON"
const val NO_BUTTON = "NO_BUTTON"

// menu
const val START_LABEL = "get a welcome message"
const val HELP_LABEL = "info how to use this bot"
const val SET_LABEL = "set your preferences"

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE