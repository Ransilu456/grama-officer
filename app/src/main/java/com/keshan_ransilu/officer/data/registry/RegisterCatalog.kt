package com.keshan_ransilu.officer.data.registry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.keshan_ransilu.officer.R

enum class FieldType {
    TEXT, NUMBER, DATE, PHONE, NIC, MULTILINE, DROPDOWN, RELATION
}

data class Validation(
    val regex: String? = null,
    val errorMessage: String? = null,
    val min: Double? = null,
    val max: Double? = null
)

data class FieldSpec(
    val key: String,
    val label: String,            // shown in Sinhala + English
    val type: FieldType,
    val required: Boolean = false,
    val options: List<String> = emptyList(),
    val relationModuleId: String? = null, // For FieldType.RELATION
    val relationDisplayField: String? = null, // Field to show in dropdown
    val validation: Validation? = null
)

data class RegisterModule(
    val id: String,
    val titleSi: String,
    val titleEn: String,
    val category: String = "General",
    val icon: ImageVector = Icons.Default.Folder,
    val iconRes: Int = R.drawable.ic_round_person,
    val fields: List<FieldSpec>
)

data class DashboardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val targetModuleId: String
)

val DashboardGridItems = listOf(
    DashboardItem(
        id = "account",
        title = "Persons",
        subtitle = "ගමේ පුද්ගලයින්",
        iconRes = R.drawable.ic_dashboard_account,
        targetModuleId = "person"
    ),
    DashboardItem(
        id = "inventory",
        title = "Inventory",
        subtitle = "ද්‍රව්‍ය හා ප්‍රවාහන",
        iconRes = R.drawable.ic_dashboard_inventory,
        targetModuleId = "permits"
    ),
    DashboardItem(
        id = "mechanic",
        title = "Search Mechanic",
        subtitle = "කාර්මික ශිල්පීන්",
        iconRes = R.drawable.ic_dashboard_mechanic,
        targetModuleId = "mechanic"
    ),
    DashboardItem(
        id = "request",
        title = "Request",
        subtitle = "ඉල්ලීම් හා සහතික",
        iconRes = R.drawable.ic_dashboard_request,
        targetModuleId = "letters"
    ),
    DashboardItem(
        id = "analytics",
        title = "Analytics",
        subtitle = "විශ්ලේෂණ හා අයවැය",
        iconRes = R.drawable.ic_dashboard_analytics,
        targetModuleId = "cashbook"
    ),
    DashboardItem(
        id = "contact",
        title = "Contact us",
        subtitle = "විමසීම් හා සබඳතා",
        iconRes = R.drawable.ic_dashboard_contact,
        targetModuleId = "contact"
    )
)

val RegisterCatalog = listOf(
    // 1. ගමේ පුද්ගලයින්
    RegisterModule(
        id = "person",
        titleSi = "ගමේ පුද්ගලයින් / ජනගහන ලේඛනය",
        titleEn = "Persons & Population Register",
        category = "Civil & Population",
        icon = Icons.Default.Person,
        iconRes = R.drawable.ic_round_person,
        fields = listOf(
            FieldSpec("fullName", "සම්පූර්ණ නම / Full Name", FieldType.TEXT, required = true),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC, required = true, 
                validation = Validation(regex = "^([0-9]{9}[xXvV]|[0-9]{12})$", errorMessage = "Invalid NIC format")),
            FieldSpec("houseId", "නිවස / House No", FieldType.RELATION,
                relationModuleId = "house", relationDisplayField = "houseNo"),
            FieldSpec("address", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE,
                validation = Validation(regex = "^0[0-9]{9}$", errorMessage = "Invalid phone number (e.g. 0712345678)")),
            FieldSpec("dob", "උපන් දිනය / DOB", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format"))
        )
    ),

    // 2. නිවාස ලේඛනය
    RegisterModule(
        id = "house",
        titleSi = "නිවාස ලේඛනය",
        titleEn = "Housing & Household Register",
        category = "Civil & Population",
        icon = Icons.Default.Home,
        iconRes = R.drawable.ic_round_house,
        fields = listOf(
            FieldSpec("houseNo", "නිවාස අංකය / House No", FieldType.TEXT, required = true),
            FieldSpec("householderId", "නිවසේ ප්‍රධානියා / Householder", FieldType.RELATION, 
                relationModuleId = "person", relationDisplayField = "fullName"),
            FieldSpec("address", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("gnDivision", "ග්‍රාම නිලධාරී වසම / GN Division", FieldType.TEXT),
            FieldSpec("memberCount", "සාමාජික ගණන / Member Count", FieldType.NUMBER,
                validation = Validation(min = 1.0))
        )
    ),

    // 3. 5. රජයේ ඉඩම් ලේඛනය
    RegisterModule(
        id = "land_gov",
        titleSi = "5. රජයේ ඉඩම් ලේඛනය",
        titleEn = "Government Lands Register",
        category = "Land Administration",
        icon = Icons.Default.Landscape,
        iconRes = R.drawable.ic_round_land,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("landName", "ඉඩමේ නම / Land Name", FieldType.TEXT, required = true),
            FieldSpec("planNo", "මූලික පිඹුරු / විස්තරාත්මක අංකය", FieldType.TEXT),
            FieldSpec("fvpLotNo", "අ.ග.පි අංකය හා කට්ටි අංකය / FVP & Lot", FieldType.TEXT),
            FieldSpec("allocatedPurpose", "ඉඩම පවරා ඇති කාරණය හා අයත් ආයතනය", FieldType.TEXT),
            FieldSpec("landNatureUsage", "ඉඩමේ ස්වභාවය හා භාවිතය / Nature & Usage", FieldType.TEXT),
            FieldSpec("extent", "ඉඩමේ ප්‍රමාණය / Extent", FieldType.TEXT),
            FieldSpec("boundaries", "මායිම් / Boundaries", FieldType.MULTILINE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 4. ඉඩම් කච්චේරි / ඉඩම් හිමිකරුවන්ගේ ලේඛනය
    RegisterModule(
        id = "land_allottees",
        titleSi = "ඉඩම් කච්චේරි / හිමිකරුවන්ගේ ලේඛනය",
        titleEn = "Land Kachcheri Allottees Register",
        category = "Land Administration",
        icon = Icons.Default.Landscape,
        iconRes = R.drawable.ic_round_land,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("allotteeName", "නඩිකරු / හිමිකරුගේ නම / Allottee", FieldType.TEXT, required = true),
            FieldSpec("address", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("grantPermitNoDate", "දීමනාපත්‍ර / බලපත්‍ර අංකය හා නිකුත් කල දිනය", FieldType.TEXT),
            FieldSpec("landName", "ඉඩමේ නම / Land Name", FieldType.TEXT),
            FieldSpec("extent", "ප්‍රමාණය / Extent", FieldType.TEXT),
            FieldSpec("fvpNo", "අ.ග.පි අංකය / FVP No", FieldType.TEXT),
            FieldSpec("lotNo", "ලොට් අංකය / Lot No", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 5. වාර්ෂික බලපත්‍රලාභීන්ගේ ලේඛනය - 75 ගොනුව
    RegisterModule(
        id = "land_permits_75",
        titleSi = "වාර්ෂික බලපත්‍රලාභීන්ගේ ලේඛනය - 75 ගොනුව",
        titleEn = "Annual Land Permit Holders (File 75)",
        category = "Land Administration",
        icon = Icons.Default.LocalShipping,
        iconRes = R.drawable.ic_round_permits,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("permitHolderName", "බලපත්‍රලාභියාගේ නම / Holder Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "වසමේ පදිංචි නම් ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("address", "පදිංචි තැනැත්තාගේ ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("fvpNo", "අ.ග.පි / FVP", FieldType.TEXT),
            FieldSpec("planLotNo", "ප්ලෑන් / ලොට් අංකය / Plan & Lot No", FieldType.TEXT),
            FieldSpec("permitNo", "බලපත්‍ර අංකය / Permit No", FieldType.TEXT),
            FieldSpec("landName", "ඉඩමේ නම / Land Name", FieldType.TEXT),
            FieldSpec("extent", "ප්‍රමාණය / Extent", FieldType.TEXT)
        )
    ),

    // 6. ඉඩම් ආරවුල් පිළිබඳ ලේඛනය
    RegisterModule(
        id = "land_disputes",
        titleSi = "ඉඩම් ආරවුල් පිළිබඳ ලේඛනය",
        titleEn = "Land Disputes & Inquiries Register",
        category = "Land Administration",
        icon = Icons.Default.Gavel,
        iconRes = R.drawable.ic_round_letters,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("complainantName", "පැමිණිලිකරුගේ නම / Complainant", FieldType.TEXT, required = true),
            FieldSpec("respondentName", "විත්තිකරුගේ නම / Respondent", FieldType.TEXT, required = true),
            FieldSpec("date", "දිනය / Date", FieldType.DATE),
            FieldSpec("disputeNature", "ආරවුලේ ස්වභාවය / Dispute Nature", FieldType.MULTILINE, required = true),
            FieldSpec("inquiryDate", "විභාග කළ දිනය / Inquiry Date", FieldType.DATE),
            FieldSpec("actionsTaken", "ගත් ක්‍රියාමාර්ග / Actions & Resolution", FieldType.MULTILINE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 7. මිනුම් තොරතුරු ලේඛනය
    RegisterModule(
        id = "land_survey",
        titleSi = "මිනුම් තොරතුරු ලේඛනය",
        titleEn = "Land Survey Information Register",
        category = "Land Administration",
        icon = Icons.Default.Straighten,
        iconRes = R.drawable.ic_round_land,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("applicantName", "ඉල්ලුම්කරුගේ නම / ආයතනය / Applicant", FieldType.TEXT, required = true),
            FieldSpec("landName", "ඉඩමේ නම / Land Name", FieldType.TEXT, required = true),
            FieldSpec("fvpPlanNo", "අ.ග.පි හා ප්ලෑන් අංකය / FVP & Plan No", FieldType.TEXT),
            FieldSpec("applicationDate", "මැනීමට අයදුම් කල දිනය / Application Date", FieldType.DATE),
            FieldSpec("dateSentToDS", "ප්‍රා.ලේ කාර්යාලයට යොමු කළ දිනය", FieldType.DATE),
            FieldSpec("surveyedDate", "මැනුම් කළ දිනය / Surveyed Date", FieldType.DATE),
            FieldSpec("surveyingInstitution", "මැනුම් කළ ආයතනය / Survey Institution", FieldType.TEXT),
            FieldSpec("surveyReferenceNo", "මැනීම් බලාගන් අංකය / Reference No", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 8. අස්වැසුම ලේඛනය
    RegisterModule(
        id = "aswasuma",
        titleSi = "අස්වැසුම ලේඛනය",
        titleEn = "Aswasuma Welfare Benefits Register",
        category = "Welfare & Benefits",
        icon = Icons.Default.VolunteerActivism,
        iconRes = R.drawable.ic_round_aswasuma,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("beneficiaryName", "ප්‍රතිලාභියාගේ නම / Beneficiary Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("familyMemberCount", "පවුලේ සාමාජික ගණන / Family Members", FieldType.NUMBER,
                validation = Validation(min = 1.0)),
            FieldSpec("categoryLevel", "ප්‍රතිලාභී කාණ්ඩය / Category", FieldType.DROPDOWN,
                options = listOf("Extreme Poor (අන්ත දිළිඳු)", "Poor (දිළිඳු)", "Vulnerable (අවදානමට ලක්වූ)", "Transitional (සංක්‍රාන්තික)")),
            FieldSpec("startDate", "දීමනාව පටන් ගත් දිනය / Start Date", FieldType.DATE),
            FieldSpec("monthlyAidAmount", "ලැබෙන මුදල / Monthly Amount (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("remarks", "වෙනත් සටහන් (පවුලේ වෙනස්වීම් ආදී)", FieldType.MULTILINE)
        )
    ),

    // 9. 17. පෝෂණ මල්ල (ගර්භණී මව්වරුන්ගේ පෝෂණ මල්ල)
    RegisterModule(
        id = "maternity_nutrition",
        titleSi = "17. ගර්භණී මව්වරුන්ගේ පෝෂණ මල්ල ලේඛනය",
        titleEn = "Maternity Nutrition Allowance Register",
        category = "Welfare & Benefits",
        icon = Icons.Default.ChildFriendly,
        iconRes = R.drawable.ic_round_birth,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("motherName", "මවගේ නම / Mother's Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("monthlyIncome", "පවුලේ මාසික ආදායම / Monthly Income", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("familyMemberCount", "පවුලේ සාමාජික ගණන / Members", FieldType.NUMBER),
            FieldSpec("clinicCardNo", "සායන කාඩ්පත් අංකය / Clinic No", FieldType.TEXT),
            FieldSpec("dateSentToDS", "ප්‍රා.ලේ.කා භාර දුන් දිනය / Sent to DS Date", FieldType.DATE),
            FieldSpec("registrationPeriod", "ලියාපදිංචි දිනය හා කාලසීමාව / Period", FieldType.TEXT),
            FieldSpec("voucherNo", "පෝෂණ වවුචර් අංකය / Voucher No", FieldType.TEXT),
            FieldSpec("voucherIssueDate", "වවුචරය භාරදුන් දිනය / Issued Date", FieldType.DATE),
            FieldSpec("recipientNameNic", "භාරගත් අය හා ජා.හැ.අ / Recipient NIC", FieldType.TEXT),
            FieldSpec("signatureStatus", "භාරගත් තත්ත්වය / Signature Status", FieldType.DROPDOWN,
                options = listOf("Signed / Received", "Pending Issue", "Transferred"))
        )
    ),

    // 10. සමෘද්ධි සහනාධාර ලේඛනය
    RegisterModule(
        id = "samurdhi",
        titleSi = "සමෘද්ධි සහනාධාර ලේඛනය",
        titleEn = "Samurdhi Allowance Register",
        category = "Welfare & Benefits",
        icon = Icons.Default.Groups,
        iconRes = R.drawable.ic_round_aswasuma,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("beneficiaryName", "ප්‍රතිලාභී නම / Beneficiary Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("familyMemberCount", "පවුලේ සාමාජික ගණන / Member Count", FieldType.NUMBER),
            FieldSpec("allowanceAmount", "දීමනා මුදල / Amount (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("startDate", "දීමනාව පටන් ගත් දිනය / Start Date", FieldType.DATE),
            FieldSpec("remarks", "වෙනත් (අත්හිටුවීම් / මාරුකිරීම් සටහන්)", FieldType.MULTILINE)
        )
    ),

    // 11. වැඩිහිටි හැඳුනුම්පත් ලේඛනය
    RegisterModule(
        id = "senior_citizen",
        titleSi = "වැඩිහිටි හැඳුනුම්පත් ලේඛනය",
        titleEn = "Senior Citizen Identity Cards Register",
        category = "Welfare & Benefits",
        icon = Icons.Default.Elderly,
        iconRes = R.drawable.ic_round_senior,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("applicationDate", "අයදුම්පත භාරගත් දිනය / App Date", FieldType.DATE),
            FieldSpec("fullName", "සම්පූර්ණ නම / Full Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC, required = true),
            FieldSpec("dob", "උපන් දිනය / DOB", FieldType.DATE),
            FieldSpec("dateSentToDS", "ප්‍රා.ලේ.කා භාර දුන් දිනය / Sent to DS", FieldType.DATE),
            FieldSpec("cardNoDate", "කාඩ්පත් අංකය හා නිකුත් කල දිනය / Card No & Date", FieldType.TEXT),
            FieldSpec("deliveredDate", "භාරදුන් දිනය / Delivered Date", FieldType.DATE),
            FieldSpec("signatureStatus", "භාරගත් බවට තහවුරුව / Status", FieldType.DROPDOWN,
                options = listOf("Delivered & Signed", "Processing at DS", "Pending Collection"))
        )
    ),

    // 12. ආධාර ලාභීන් මිය ගිය අයගේ නාම ලේඛනය
    RegisterModule(
        id = "deceased_beneficiaries",
        titleSi = "ආධාර ලාභීන් මිය ගිය අයගේ නාම ලේඛනය",
        titleEn = "Deceased Beneficiaries Register",
        category = "Welfare & Benefits",
        icon = Icons.Default.PersonOff,
        iconRes = R.drawable.ic_round_death,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("deceasedName", "මියගිය තැනැත්තාගේ නම / Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("schemeType", "ආධාර වර්ගය / Scheme Type", FieldType.DROPDOWN,
                options = listOf("Aswasuma (අස්වැසුම)", "Samurdhi (සමෘද්ධි)", "Senior Citizen (වැඩිහිටි)", "Maternity Nutrition (පෝෂණ මල්ල)")),
            FieldSpec("cardNo", "කාඩ්පත් / ප්‍රතිලාභී අංකය / Card No", FieldType.TEXT),
            FieldSpec("dateReportedToDS", "ප්‍රා.ලේ.කා වාර්තා කල දිනය / Reported Date", FieldType.DATE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 13. ජ්‍යෙෂ්ඨ නිලධාරීන්ගේ වසමට පැමිණීමේ සටහන්
    RegisterModule(
        id = "officer_visits",
        titleSi = "ජ්‍යෙෂ්ඨ නිලධාරීන් වසමට පැමිණීමේ සටහන්",
        titleEn = "Senior Officers Division Visits Log",
        category = "Administration",
        icon = Icons.Default.EventNote,
        iconRes = R.drawable.ic_round_contact,
        fields = listOf(
            FieldSpec("date", "දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("officerNameDesignation", "නිලධාරියාගේ නම හා තනතුර / Name & Designation", FieldType.TEXT, required = true),
            FieldSpec("purposeOfVisit", "පැමිණි ස්ථානය හා කාර්යය / Location & Purpose", FieldType.TEXT, required = true),
            FieldSpec("inspectionNotes", "පරීක්ෂණ සහතික කිරීම් සටහන් / Inspection Notes", FieldType.MULTILINE, required = true)
        )
    ),

    // 14. ස්වේච්ඡා සංවිධාන හා පොදු වැඩසටහන්
    RegisterModule(
        id = "voluntary_orgs",
        titleSi = "ස්වේච්ඡා සංවිධාන හා පොදු වැඩසටහන්",
        titleEn = "Voluntary Organizations & Programs",
        category = "Administration",
        icon = Icons.Default.Diversity3,
        iconRes = R.drawable.ic_round_person,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("orgNameStartedDate", "සංවිධානයේ නම හා ආරම්භක දිනය", FieldType.TEXT, required = true),
            FieldSpec("presidentNamePhone", "සභාපති: නම හා දුරකථනය / President", FieldType.TEXT),
            FieldSpec("secretaryNamePhone", "ලේකම්: නම හා දුරකථනය / Secretary", FieldType.TEXT),
            FieldSpec("treasurerNamePhone", "භාණ්ඩාගාරික: නම හා දුරකථනය / Treasurer", FieldType.TEXT),
            FieldSpec("regNoDate", "ලියාපදිංචි අංකය හා දිනය / Reg No & Date", FieldType.TEXT),
            FieldSpec("memberCount", "සාමාජිකයන් ගණන / Member Count", FieldType.NUMBER),
            FieldSpec("purposeObjectives", "ස්වභාවය / අරමුණ / Purpose & Objectives", FieldType.MULTILINE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 15. ලැබෙන/යවන ලිපි හා ඉල්ලීම්
    RegisterModule(
        id = "letters",
        titleSi = "ලැබෙන/යවන ලිපි හා ඉල්ලීම්",
        titleEn = "Requests & Letters",
        category = "Administration",
        icon = Icons.Default.Email,
        iconRes = R.drawable.ic_round_letters,
        fields = listOf(
            FieldSpec("refNo", "අංකය / Ref No", FieldType.TEXT, required = true),
            FieldSpec("date", "දිනය / Date", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("subject", "විෂය / Subject", FieldType.TEXT, required = true),
            FieldSpec("fromTo", "කාගෙන්/කාටද / From/To", FieldType.TEXT),
            FieldSpec("status", "තත්ත්වය / Status", FieldType.DROPDOWN,
                options = listOf("Pending", "Approved", "In Progress", "Completed", "Rejected")),
            FieldSpec("remarks", "සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 16. ද්‍රව්‍ය හා ප්‍රවාහන බලපත්‍ර
    RegisterModule(
        id = "permits",
        titleSi = "ද්‍රව්‍ය හා ප්‍රවාහන බලපත්‍ර",
        titleEn = "Inventory & Transport Permits",
        category = "Administration",
        icon = Icons.Default.LocalShipping,
        iconRes = R.drawable.ic_round_permits,
        fields = listOf(
            FieldSpec("holderId", "බලපත්‍රලාභියා / Holder", FieldType.RELATION, 
                relationModuleId = "person", relationDisplayField = "fullName"),
            FieldSpec("item", "ද්‍රව්‍යය / Inventory Item", FieldType.TEXT, required = true),
            FieldSpec("vehicleNo", "වාහන අංකය / Vehicle No", FieldType.TEXT,
                validation = Validation(regex = "^[A-Z]{1,3}-[0-9]{4}$", errorMessage = "Invalid vehicle format")),
            FieldSpec("quantity", "ප්‍රමාණය / Quantity", FieldType.TEXT),
            FieldSpec("date", "දිනය / Date", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("destination", "ගමනාන්තය / Destination", FieldType.TEXT)
        )
    ),

    // 17. උපත් වාර්තා ලේඛනය
    RegisterModule(
        id = "birth",
        titleSi = "උපත් වාර්තා ලේඛනය",
        titleEn = "Birth Records",
        category = "Civil & Population",
        icon = Icons.Default.ChildCare,
        iconRes = R.drawable.ic_round_birth,
        fields = listOf(
            FieldSpec("childName", "ළදරුවාගේ නම / Child Name", FieldType.TEXT, required = true),
            FieldSpec("fatherId", "පියා / Father", FieldType.RELATION, 
                relationModuleId = "person", relationDisplayField = "fullName"),
            FieldSpec("motherId", "මව / Mother", FieldType.RELATION, 
                relationModuleId = "person", relationDisplayField = "fullName"),
            FieldSpec("dob", "උපන් දිනය / DOB", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("regNo", "ලියාපදිංචි අංකය / Reg No", FieldType.TEXT)
        )
    ),

    // 18. මරණ වාර්තා ලේඛනය
    RegisterModule(
        id = "death",
        titleSi = "මරණ වාර්තා ලේඛනය",
        titleEn = "Death Records",
        category = "Civil & Population",
        icon = Icons.Default.Warning,
        iconRes = R.drawable.ic_round_death,
        fields = listOf(
            FieldSpec("personId", "මියගිය තැනැත්තා / Deceased Person", FieldType.RELATION, 
                relationModuleId = "person", relationDisplayField = "fullName"),
            FieldSpec("dateOfDeath", "මියගිය දිනය / Date of Death", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("placeOfDeath", "ස්ථානය / Place", FieldType.TEXT),
            FieldSpec("cause", "හේතුව / Cause", FieldType.TEXT),
            FieldSpec("certNo", "සහතික අංකය / Cert No", FieldType.TEXT)
        )
    ),

    // 19. මුදල් පොත හා විශ්ලේෂණ
    RegisterModule(
        id = "cashbook",
        titleSi = "මුදල් පොත හා විශ්ලේෂණ",
        titleEn = "Analytics & Cash Book",
        category = "Administration",
        icon = Icons.Default.AccountBalanceWallet,
        iconRes = R.drawable.ic_round_cashbook,
        fields = listOf(
            FieldSpec("date", "දිනය / Date", FieldType.DATE, required = true,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("received", "ලැබුණු මුදල / Received (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("paid", "ගෙවූ මුදල / Paid (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("purpose", "අරමුණ / Purpose", FieldType.TEXT, required = true),
            FieldSpec("receiptNo", "ලදුපත් අංකය / Receipt No", FieldType.TEXT)
        )
    ),

    // 20. කාර්මික සේවා
    RegisterModule(
        id = "mechanic",
        titleSi = "කාර්මික සේවා හා ශිල්පීන්",
        titleEn = "Search Mechanic & Services",
        category = "Directory",
        icon = Icons.Default.Build,
        iconRes = R.drawable.ic_round_mechanic,
        fields = listOf(
            FieldSpec("fullName", "නම / Full Name", FieldType.TEXT, required = true),
            FieldSpec("ratePerHour", "පැයක ගාස්තුව / Rate (LKR)", FieldType.NUMBER, required = true,
                validation = Validation(min = 1.0)),
            FieldSpec("specialty", "විශේෂඥතාව / Specialty", FieldType.DROPDOWN,
                options = listOf("Automotive Repair", "Heavy Machinery", "Electrical & Wiring", "Welding & Fabrication", "Hydraulics")),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE, required = true),
            FieldSpec("rating", "ශ්‍රේණිගත කිරීම / Rating (1-5)", FieldType.NUMBER),
            FieldSpec("location", "ස්ථානය / Location", FieldType.TEXT),
            FieldSpec("availability", "පවතින බව / Status", FieldType.DROPDOWN,
                options = listOf("Available", "On Job", "Offline")),
            FieldSpec("remarks", "සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 21. විමසීම් හා සබඳතා
    RegisterModule(
        id = "contact",
        titleSi = "විමසීම් හා සබඳතා",
        titleEn = "Contact & Inquiries",
        category = "Directory",
        icon = Icons.Default.Phone,
        iconRes = R.drawable.ic_round_contact,
        fields = listOf(
            FieldSpec("contactName", "නම / Contact Person", FieldType.TEXT, required = true),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE, required = true),
            FieldSpec("email", "විද්‍යුත් තැපෑල / Email", FieldType.TEXT),
            FieldSpec("department", "අංශය / Department", FieldType.DROPDOWN,
                options = listOf("Administration", "Technical Support", "Social Welfare", "Public Services", "Emergency Hotline")),
            FieldSpec("message", "පණිවිඩය / Notes", FieldType.MULTILINE)
        )
    )
)
