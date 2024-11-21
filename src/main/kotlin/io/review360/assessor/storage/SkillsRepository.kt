package io.review360.assessor.storage

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.util.logging.*
import io.review360.assessor.model.Skill
import io.review360.assessor.plugins.JsonMapper.defaultMapper
import java.io.File

/*
 * In-memory container for skills list
 */
data object SkillsRepository: FileRepository<Skill> {
    private val skills = mutableSetOf<Skill>()
    private val LOGGER = KtorSimpleLogger("io.review360.assessor.storage.SkillsRepository")

    override fun init() {
        val jsonString = File("db/Skills.json").readText(Charsets.UTF_8)
        val skillsFromFile = defaultMapper.readValue<Set<Skill>>(jsonString)
        LOGGER.trace("Loading skills...")
        for (skill in skillsFromFile) {
            val duplicates = skills.findLast { it.code == skill.code }
            if (duplicates == null) {
                skills.add(skill)
                LOGGER.trace("- loaded: {}", skill)
            } else {
                LOGGER.error("- Skill from file {} duplicates loaded skill {}! Check your Skills.json .", skill, duplicates)
            }
        }
    }

    fun getByCode(code: String): Skill? = skills.findLast { it.code == code }

    override fun getAll(): Set<Skill> = skills
}