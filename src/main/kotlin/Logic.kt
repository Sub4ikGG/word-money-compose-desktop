import kotlin.Exception

fun main() {
    try {
        val word = "funfhundert zwei und zehn".lowercase() // 13
        val int = convertWordToInt(word = word)

        println("Ответ: $int")
    }
    catch (e: Exception) {
        println(e.localizedMessage)
    }
}

fun convertWordToInt(word: String): Int {
    if (word.isTenFirst().isNotEmpty()) {
        var split = word.isTenFirst()
        val first = split.first()
        val ten = first.toTen()
        split = split.drop(1)

        if (split.isNotEmpty())
            throw Exception("После десяток не могут идти значения")
        else return ten
    }

    if (word.isHundredFirst().isNotEmpty()) {
        var split2 = word.isHundredFirst()
        val first = split2.first()
        val hundred = first.toHundred()
        split2 = split2.drop(1)

        if (split2.joinToString("").startsWith("und"))
            throw WordException("После hundert не может идти und")

        var split3 = split2.joinToString("").isUnitFirst()
        if (split3.isNotEmpty()) { // drei hundert zwei
            val unit2 = split3.first().toUnit() // zwei 2
            split3 = split3.drop(1)

            val divider = split3.joinToString("").startsWith("und")
            if (divider) { // und
                val split4 = split3.joinToString("").removePrefix("und")

                if (split4.isBlank())
                    throw WordException("После 'und' должен быть десяток")

                if (split4.trim().isTen())
                    return hundred + split4.trim().toTen() + unit2
                else throw WordException("Ошибка при вводе '${split4.trim()}'")
            }

            if (split3.isEmpty()) return hundred + unit2 // 302
        }

        if (split2.joinToString("").contains("und")) {
            val first2 = split2.joinToString("").split("und").first().trim()
            throw WordException("Ошибка при вводе $first2")
        }

        if (split2.joinToString("").isBlank())
            return hundred

        if (split2.joinToString("") == GermanDictionary.DIVIDER)
            throw WordException("Перед 'und' должны идти единицы, а после десятки")

        val simple = checkIsSimple(split2.joinToString("").trim())
        return hundred + simple
    }

    var split = word.isUnitFirst()
    if (split.isNotEmpty() && split.first() != "null") { // drei 3
        val unit = split.first().toUnit().takeIf { it != 0 } ?: throw WordException("Нулевой множитель") // drei
        split = split.drop(1)

        var split2 = split.joinToString("").isHundredFirst()
        if (split2.isNotEmpty()) { // drei hundert
            val hundred = unit * split2.first().toHundred() // hundert 300
            split2 = split2.drop(1)

            if (split2.joinToString("").startsWith("und"))
                throw WordException("После hundert не может идти und")

            var split3 = split2.joinToString("").isUnitFirst()
            if (split3.isNotEmpty()) { // drei hundert zwei
                val unit2 = split3.first().toUnit() // zwei 2
                split3 = split3.drop(1)

                val divider = split3.joinToString("").startsWith("und")
                if (divider) { // und
                    val split4 = split3.joinToString("").removePrefix("und")

                    if (split4.isBlank())
                        throw WordException("После 'und' должен быть десяток")

                    if (split4.trim().isTen())
                        return hundred + split4.trim().toTen() + unit2
                    else throw WordException("Ошибка при вводе '${split4.trim()}'")
                }

                if (split3.isEmpty()) return hundred + unit2 // 302
            }

            if (split2.joinToString("").contains("und")) {
                val first = split2.joinToString("").split("und").first().trim()
                throw WordException("Ошибка при вводе $first")
            }

            if (split2.joinToString("").isBlank())
                return hundred

            if (split2.joinToString("") == GermanDictionary.DIVIDER)
                throw WordException("Перед 'und' должны идти единицы, а после десятки")

            val simple = checkIsSimple(split2.joinToString("").trim())
            return hundred + simple
        }

        val divider = split.joinToString("").startsWith("und")
        if (divider) { // und
            val split3 = split.joinToString("").removePrefix("und")

            if (split3.isEmpty()) throw WordException("После 'und' должен быть десяток")

            if (split3.trim().isTen())
                return split3.trim().toTen() + unit
            else throw WordException("Ошибка при вводе  ${split3.trim()}")
        }

        if (split.joinToString("").isUnit())
            throw WordException("После единиц не могут идти единицы")

        if (split.joinToString("").isTen())
            throw WordException("Пропущен 'und' между словами")

        if (split.joinToString("").isNotBlank())
            throw WordException("Ошибка при вводе ${split.joinToString("")}")

        return unit
    }

    if (word.isHundred())
        return word.toHundred()

    if (word == GermanDictionary.DIVIDER) {
        throw WordException("Перед 'und' должны идти единицы, а после десятки")
    }

    if (word.contains("und")) {
        val first = word.split("und").first().trim()
        throw WordException("Ошибка при вводе $first")
    }

    try {
        return checkIsSimple(word.fit())
    } catch (e: Exception) {
        throw WordException("После единиц не могут идти выражения, только сотни")
    }
//    if (word.containsHundred()) {
//        var splitWord = word.split("hundert").fit()
//
//        val hundred = checkMultiplier(multiplier = splitWord.first()) * 100
//        splitWord = splitWord.drop(1).fit()
//
//        if (splitWord.isEmpty()) return hundred
//
//        val afterHundred = splitWord.joinToString("")
//        if (afterHundred.containsDivider()) { // Включает в себя und => двузначное число
//            splitWord = afterHundred.split(GermanDictionary.DIVIDER).fit()
//
//            if (splitWord.first().isUnit()) {
//                val unit = splitWord.first().toUnit()
//
//                if (splitWord.last().isTen()) {
//                    val ten = splitWord.last().toTen()
//                    return hundred + ten + unit
//                } else {
//                    val whatIsIt = splitWord.filter { it != splitWord.first() }
//                    if (whatIsIt.isEmpty()) throw WordException("После 'und' должен быть десяток")
//                    else throw WordException("Неизвестное значение после 'und'")
//                }
//            } else throw WordException("Неизвестное значение '${splitWord.first()}' перед 'und'")
//        } else {
//            val simple = checkIsSimple(word = afterHundred)
//            return hundred + simple
//        }
//    }
//
//    if (word.containsDivider()) { // Включает в себя und => двузначное число
//        val splitWord = word.split(GermanDictionary.DIVIDER).fit()
//
//        if (splitWord.first().isUnit()) {
//            val unit = splitWord.first().toUnit()
//
//            if (splitWord.last().isTen()) {
//                val ten = splitWord.last().toTen()
//                return ten + unit
//            } else {
//                val whatIsIt = splitWord.filter { it != splitWord.first() }
//                if (whatIsIt.isEmpty()) throw WordException("После 'und' должен быть десяток")
//                else throw WordException("Неизвестное значение после 'und'")
//            }
//        } else throw WordException("'${splitWord.first()}' включает в себя ошибку")
//    }
//
//    return checkIsSimple(word = word)
}

private fun String.isUnitFirst(): List<String> {
    GermanDictionary.units.forEach { (key, _) ->
        if (this.startsWith(key)) {
            val list = this.split(key, limit = 2).toMutableList()
            list.add(0, key)
            return list.fit()
        }
    }

    return emptyList()
}

private fun String.isTenFirst(): List<String> {
    GermanDictionary.tens.forEach { (key, _) ->
        if (this.startsWith(key)) {
            val list = this.split(key).toMutableList()
            list.add(0, key)
            return list.fit()
        }
    }

    return emptyList()
}

private fun String.isMutantFirst(): List<String> {
    GermanDictionary.mutants.forEach { (key, _) ->
        if (this.startsWith(key)) {
            val list = this.split(key).toMutableList()
            list.add(0, key)
            return list.fit()
        }
    }

    return emptyList()
}

private fun String.isHundredFirst(): List<String> {
    GermanDictionary.hundreds.forEach { (key, _) ->
        if (this.startsWith("hundert")) {
            val list = this.split(key).toMutableList()
            list.add(0, key)
            return list.fit()
        }
    }

    return emptyList()
}

private fun String.containsHundred() = this.contains("hundert")
private fun String.containsDivider() = this.contains(GermanDictionary.DIVIDER)

fun checkMultiplier(multiplier: String): Int {
    if (multiplier == "null") throw Exception("Не может быть нулевого множителя $multiplier")
    if (multiplier.isUnit()) return multiplier.toUnit()
    throw Exception("'$multiplier' неизвестный множитель")
}

fun checkIsSimple(word: String): Int {
    if (word.isUnit()) return word.toUnit()
    if (word.isTen()) return word.toTen()
    if (word.isMutant()) return word.toMutant()
    throw Exception("'$word' неизвестное значение")
}

private fun String.isUnit() = GermanDictionary.units[this] != null
private fun String.isTen() = GermanDictionary.tens[this] != null
private fun String.isMutant() = GermanDictionary.mutants[this] != null
private fun String.isHundred() = GermanDictionary.hundreds[this] != null

private fun String.toUnit() = GermanDictionary.units[this]!!
private fun String.toTen() = GermanDictionary.tens[this]!!
private fun String.toMutant() = GermanDictionary.mutants[this]!!
private fun String.toHundred() = GermanDictionary.hundreds[this]!!

fun String.fit() = this.split(" ").filter { it.isNotBlank() }.joinToString("")
fun List<String>.fit() = this.filter { it.isNotBlank() }.map { it.trim() }
