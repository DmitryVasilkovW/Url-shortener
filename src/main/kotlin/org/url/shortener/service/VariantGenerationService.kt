package org.url.shortener.org.url.shortener.service

import org.apache.commons.codec.language.DoubleMetaphone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VariantGenerationService @Autowired constructor(
    private val urlExistenceValidator: UrlExistenceValidator
) {

    private val doubleMetaphone = DoubleMetaphone()
    private val suffixes = arrayOf("ing", "ed", "ion", "tion", "ness", "ly", "ment", "able", "ible", "er")
    private val prefixes = arrayOf("un", "re", "in", "im", "dis", "non")
    private val maximumNumberOfGeneratedVariants = 5

    fun generateVariants(input: String): Array<String> {
        val variants = mutableListOf<String>()
        var generated = 0
        var attempts = 0

        while (generated < maximumNumberOfGeneratedVariants && attempts < input.length * maximumNumberOfGeneratedVariants) {
            var variantGenerated = false

            for (i in input.indices) {
                val originalChar = input[i]
                val chars = charArrayOf('a', 'e', 'i', 'o', 'u')
                for (c in chars) {
                    if (c != originalChar) {
                        val variant = input.substring(0, i) + c + input.substring(i + 1)
                        if (addVariant(variants, variant, input)) {
                            generated++
                            variantGenerated = true
                            break
                        }
                    }
                }
                if (variantGenerated) break
            }

            if (generated < maximumNumberOfGeneratedVariants) {
                for (suffix in suffixes) {
                    val variant = input + suffix
                    if (addVariant(variants, variant, input)) {
                        generated++
                        break
                    }
                }
            }

            if (generated < maximumNumberOfGeneratedVariants) {
                for (prefix in prefixes) {
                    val variant = prefix + input
                    if (addVariant(variants, variant, input)) {
                        generated++
                        break
                    }
                }
            }

            if (generated < maximumNumberOfGeneratedVariants) {
                for (suffix in suffixes) {
                    if (input.endsWith(suffix)) {
                        val variant = input.substring(0, input.length - suffix.length)
                        if (addVariant(variants, variant, input)) {
                            generated++
                            break
                        }
                    }
                }
            }

            attempts++
        }

        return variants.toTypedArray()
    }

    private fun addVariant(variants: MutableList<String>, variant: String, original: String): Boolean {
        val variantMetaphone = doubleMetaphone.doubleMetaphone(variant)
        val originalMetaphone = doubleMetaphone.doubleMetaphone(original)

        return if (!isVariantInDatabase(variant) && variantMetaphone == originalMetaphone) {
            variants.add(variant)
            true
        } else {
            false
        }
    }

    private fun isVariantInDatabase(variantMetaphone: String): Boolean {
        return urlExistenceValidator.validateAndGetError(variantMetaphone) != null
    }
}
