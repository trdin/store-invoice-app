package util

import kotlin.math.ceil

/**
 * *Singleton Class*
 * A util class for *barcode*.
 * Working with barcodes
 *
 * @property barcodeCountry a hashmap that contains country codes for barcodes
 *
 */

object BarcodeUtil {
    /**
     * Checks if barcode is valid.
     * @param
     * @return boolean; true if barcode is valid and false if it is not.
     */
    fun isBarcodeValid(barcode: String): Boolean {
        if (barcode.length == 8 || (barcode.length in 12..14) || barcode.length == 17 || barcode.length == 18) {
            var i = 0
            var sum = 0.0
            while (i < barcode.length - 2) {
                sum += barcode[i].digitToInt()
                if (i + 1 != barcode.length - 1) {
                    sum += barcode[i + 1].digitToInt() * 3
                }
                i += 2
            }
            val ten = ceil(sum / 10.0) * 10
            if ((ten - sum).toInt() == barcode[barcode.length - 1].digitToInt()) {
                return true
            }
        }
        return false
    }

    /**
     * finds a country name from the barcode
     * @param barcode form which its extracts the country code
     * @return string; country name or not found if there is no country with that code.
     */
    fun getCompanyCountryFromBarcode(barcode: String): String {
        val countryNum = barcode.substring(0, 3)
        return if (barcodeCountry[countryNum] != null) {
            barcodeCountry.get(countryNum).toString()
        } else {
            "country not found"
        }
    }

    private val barcodeCountry: HashMap<String, String> = hashMapOf(
        "000" to "USA and Canada",
        "100" to "USA",
        "300" to "France and Monaco",
        "310" to "France and Monaco",
        "320" to "France and Monaco",
        "330" to "France and Monaco",
        "340" to "France and Monaco",
        "350" to "France and Monaco",
        "360" to "France and Monaco",
        "370" to "France and Monaco",
        "380" to "Bulgaria",
        "383" to "Slovenia",
        "385" to "Croatia",
        "387" to "Bosnia and Herzegovina",
        "389" to "Montenegro",
        "400" to "Germany",
        "410" to "Germany",
        "420" to "Germany",
        "430" to "Germany",
        "440" to "Germany",
        "450" to "Japan",
        "460" to "Russia",
        "470" to "Kyrgyzstan",
        "471" to "Taiwan",
        "474" to "Estonia",
        "475" to "Latvia",
        "476" to "Azerbaijan",
        "477" to "Lithuania",
        "478" to "Uzbekistan",
        "479" to "Sri Lanka",
        "480" to "Philippines",
        "481" to "Belarus",
        "482" to "Ukraine",
        "483" to "Turkmenistan",
        "484" to "Moldova",
        "485" to "Armenia",
        "486" to "Georgia",
        "487" to "Kazakhstan",
        "488" to "Tajikistan",
        "489" to "Hong Kong",
        "490" to "Japan",
        "500" to "United Kingdom",
        "520" to "Greece",
        "521" to "Greece",
        "528" to "Lebanon",
        "529" to "Cyprus",
        "530" to "Albania",
        "531" to "North Macedonia",
        "535" to "Malta",
        "539" to "Ireland",
        "540" to "Belgium and Luxembourg",
        "560" to "Portugal",
        "569" to "Iceland",
        "570" to "Denmark",
        "590" to "Poland",
        "594" to "Romania",
        "599" to "Hungary",
        "600, 601" to "South Africa",
        "603" to "Ghana",
        "604" to "Senegal",
        "608" to "Bahrain",
        "609" to "Mauritius",
        "611" to "Morocco",
        "613" to "Algeria",
        "615" to "Nigeria",
        "616" to "Kenya",
        "618" to "CÃ´te d'Ivoire",
        "619" to "Tunisia",
        "620" to "Tanzania",
        "621" to "Syria",
        "622" to "Egypt",
        "623" to "Brunei",
        "624" to "Libya",
        "625" to "Jordan",
        "626" to "Iran",
        "627" to "Kuwait",
        "628" to "Saudi Arabia",
        "629" to "United Arab Emirates",
        "630" to "Qatar",
        "640" to "Finland",
        "690" to "China",
        "700" to "Norway",
        "729" to "Israel",
        "730" to "Sweden",
        "740" to "Guatemala",
        "741" to "El Salvador",
        "742" to "Honduras",
        "743" to "Nicaragua",
        "744" to "Costa Rica",
        "745" to "Panama",
        "746" to "Dominican Republic",
        "750" to "Mexico",
        "754" to "Canada",
        "755" to "Canada",
        "759" to "Venezuela",
        "760" to "Switzerland and Liechtenstein",
        "770" to "Colombia",
        "771" to "Colombia",
        "773" to "Uruguay",
        "775" to "Peru",
        "777" to "Bolivia",
        "778" to "Argentina",
        "779" to "Argentina",
        "780" to "Chile",
        "784" to "Paraguay",
        "786" to "Ecuador",
        "789" to "Brazil",
        "790" to "Brazil",
        "800" to "Italy, San Marino, and Vatican City",
        "810" to "Italy, San Marino, and Vatican City",
        "820" to "Italy, San Marino, and Vatican City",
        "830" to "Italy, San Marino, and Vatican City",
        "840" to "Spain, Andorra",
        "850" to "Cuba",
        "858" to "Slovakia",
        "859" to "Czech Republic",
        "860" to "Serbia",
        "865" to "Mongolia",
        "867" to "North Korea",
        "868" to "Turkey",
        "869" to "Turkey",
        "870" to "Netherlands",
        "880" to "South Korea",
        "883" to "Myanmar",
        "884" to "Cambodia",
        "885" to "Thailand",
        "888" to "Singapore",
        "890" to "India",
        "893" to "Vietnam",
        "894" to "Bangladesh",
        "896" to "Pakistan",
        "899" to "Indonesia",
        "900" to "Austria",
        "910" to "Austria",
        "930" to "Australia",
        "940" to "New Zealand",
        "955" to "Malaysia",
        "958" to "Macau",
        "960" to "GS1 Global Office: GTIN-8 allocations",
        "977" to "Serial publications",
        "978" to "\"Bookland\"",
        "979" to "\"Bookland\"",
        "980" to "Refund receipts"
    )
}