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
        id = "persons",
        title = "Persons",
        subtitle = "ගමේ පුද්ගලයින්",
        iconRes = R.drawable.ic_dashboard_account,
        targetModuleId = "person"
    ),
    DashboardItem(
        id = "letters",
        title = "Requests & Letters",
        subtitle = "04. ලැබෙන/යවන ලිපි",
        iconRes = R.drawable.ic_dashboard_request,
        targetModuleId = "letters"
    ),
    DashboardItem(
        id = "cashbook",
        title = "Cash Book",
        subtitle = "01. මුදල් පොත",
        iconRes = R.drawable.ic_dashboard_analytics,
        targetModuleId = "cashbook"
    ),
    DashboardItem(
        id = "permits",
        title = "Permit Register",
        subtitle = "06. බලපත්‍ර නිර්දේශ",
        iconRes = R.drawable.ic_dashboard_inventory,
        targetModuleId = "permit_recommendations"
    ),
    DashboardItem(
        id = "business",
        title = "Business Registry",
        subtitle = "14. ව්‍යාපාර ලියාපදිංචිය",
        iconRes = R.drawable.ic_round_business,
        targetModuleId = "business_registrations"
    ),
    DashboardItem(
        id = "pensions",
        title = "Pensions",
        subtitle = "16. විශ්‍රාම වැටුප් ලේඛනය",
        iconRes = R.drawable.ic_round_pension,
        targetModuleId = "pension_registry"
    ),
    DashboardItem(
        id = "lands",
        title = "Gov Lands",
        subtitle = "05. රජයේ ඉඩම්",
        iconRes = R.drawable.ic_round_land,
        targetModuleId = "land_gov"
    ),
    DashboardItem(
        id = "allowances",
        title = "Allowances",
        subtitle = "07. රජයේ දීමනා",
        iconRes = R.drawable.ic_round_allowance,
        targetModuleId = "gov_allowances"
    ),
    DashboardItem(
        id = "vital_events",
        title = "Birth & Death",
        subtitle = "15. උපත් හා මරණ",
        iconRes = R.drawable.ic_round_birth,
        targetModuleId = "birth_death_reports"
    ),
    DashboardItem(
        id = "nutrition",
        title = "Nutrition Aid",
        subtitle = "17. පෝෂණ දීමනා",
        iconRes = R.drawable.ic_round_birth,
        targetModuleId = "maternity_nutrition"
    ),
    DashboardItem(
        id = "projects",
        title = "Projects",
        subtitle = "11. සංවර්ධන ව්‍යාපෘති",
        iconRes = R.drawable.ic_round_projects,
        targetModuleId = "development_projects"
    ),
    DashboardItem(
        id = "contact",
        title = "Contact & Services",
        subtitle = "විමසීම් හා සබඳතා",
        iconRes = R.drawable.ic_dashboard_contact,
        targetModuleId = "contact"
    )
)

val RegisterCatalog = listOf(
    // CORE POPULATION REGISTERS
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
            FieldSpec("gender", "ස්ත්‍රී / පුරුෂ භාවය / Gender", FieldType.DROPDOWN,
                options = listOf("Male (පුරුෂ)", "Female (ස්ත්‍රී)")),
            FieldSpec("occupation", "රැකියාව / Occupation", FieldType.TEXT),
            FieldSpec("address", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE,
                validation = Validation(regex = "^0[0-9]{9}$", errorMessage = "Invalid phone number (e.g. 0712345678)")),
            FieldSpec("dob", "උපන් දිනය / DOB", FieldType.DATE,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format"))
        )
    ),

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

    // 01. මුදල් පොත
    RegisterModule(
        id = "cashbook",
        titleSi = "01. මුදල් පොත",
        titleEn = "01. Cash Book Register",
        category = "Finance & Administration",
        icon = Icons.Default.AccountBalanceWallet,
        iconRes = R.drawable.ic_round_cashbook,
        fields = listOf(
            FieldSpec("date", "දිනය / Date", FieldType.DATE, required = true,
                validation = Validation(regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", errorMessage = "Use YYYY-MM-DD format")),
            FieldSpec("transactionType", "ගනුදෙනු වර්ගය / Type", FieldType.DROPDOWN, required = true,
                options = listOf("Collection (මුදල් අය කිරීම)", "Disbursement (මුදල් බාර කිරීම / ගෙවීම)")),
            FieldSpec("receiptNo", "ලදුපත් අංකය / Receipt No", FieldType.TEXT, required = true),
            FieldSpec("payerPayeeNameAddress", "ගෙවූ / බාරගත් අයගේ නම හා ලිපිනය", FieldType.MULTILINE, required = true),
            FieldSpec("purpose", "අය කල / ගෙවූ කාරණය / Purpose", FieldType.TEXT, required = true),
            FieldSpec("received", "අය කල මුදල / Received (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("paid", "බාර කල මුදල / Paid (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("handedOverDate", "මුදල් භාරකළ දිනය / Handed Over Date", FieldType.DATE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 02. විෂය ගොනු ලේඛනය
    RegisterModule(
        id = "subject_files",
        titleSi = "02. විෂය ගොනු ලේඛනය",
        titleEn = "02. Subject Files Register",
        category = "Administration",
        icon = Icons.Default.FolderSpecial,
        iconRes = R.drawable.ic_round_files,
        fields = listOf(
            FieldSpec("serialNo", "අනුක්‍රමික අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("fileNo", "ලිපි ගොනු අංකය / File No", FieldType.TEXT, required = true),
            FieldSpec("fileName", "ලිපි ගොනුවේ නම / Subject File Name", FieldType.TEXT, required = true),
            FieldSpec("startDate", "ආරම්භ කළ දිනය / Start Date", FieldType.DATE),
            FieldSpec("locationRack", "ගොනුව තැන්පත් ස්ථානය / Rack Location", FieldType.TEXT),
            FieldSpec("status", "තත්ත්වය / Status", FieldType.DROPDOWN,
                options = listOf("Active (ක්‍රියාකාරී)", "Archived (ලේඛනාගාර)", "Transferred (යොමු කළ)")),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 03. බඩු වවුචර් ලේඛනය, උපපත්ති හා අංකිත ආකෘති ලේඛනය
    RegisterModule(
        id = "vouchers_forms",
        titleSi = "03. බඩු වවුචර් හා අංකිත ආකෘති ලේඛනය",
        titleEn = "03. Vouchers & Counterfoil Forms Register",
        category = "Administration",
        icon = Icons.Default.ReceiptLong,
        iconRes = R.drawable.ic_round_vouchers,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("formType", "උපපත්‍රිකා / අංකිත ආකෘති වර්ගය / Form Type", FieldType.TEXT, required = true),
            FieldSpec("acquiredDateAndIssuedBy", "ලබා ගත් දිනය හා නිකුත් කල අය", FieldType.TEXT),
            FieldSpec("printedNumberFrom", "මුද්‍රිත අංක: සිට / From", FieldType.TEXT),
            FieldSpec("printedNumberTo", "මුද්‍රිත අංක: දක්වා / To", FieldType.TEXT),
            FieldSpec("lastUsedDate", "භාවිතය අවසන් වූ දිනය / Last Used Date", FieldType.DATE),
            FieldSpec("disposedOrHandedOverDatePerson", "අපහරණය / භාරගත් අය හා දිනය", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 04. ලැබෙන හා යවන ලිපි ලේඛනය
    RegisterModule(
        id = "letters",
        titleSi = "04. ලැබෙන හා යවන ලිපි ලේඛනය",
        titleEn = "04. Inward & Outward Letters Register",
        category = "Administration",
        icon = Icons.Default.Email,
        iconRes = R.drawable.ic_round_letters,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("letterType", "ලිපි වර්ගය / Letter Type", FieldType.DROPDOWN, required = true,
                options = listOf("Inward (ලැබෙන ලිපි)", "Outward (යවන ලිපි)")),
            FieldSpec("date", "දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("letterRefNoAndDate", "ලිපියේ අංකය හා දිනය / Letter Ref & Date", FieldType.TEXT, required = true),
            FieldSpec("senderOrReceiver", "ලැබුනේ කාගෙන්ද / යැව්වේ කාටද", FieldType.TEXT, required = true),
            FieldSpec("contentSummary", "ලිපියේ අන්තර්ගතය / Content Summary", FieldType.MULTILINE, required = true),
            FieldSpec("actionTaken", "සිදු කල රාජකාරි / Action Taken", FieldType.MULTILINE),
            FieldSpec("fileNo", "ගොනු අංකය / File No", FieldType.TEXT),
            FieldSpec("status", "තත්ත්වය / Status", FieldType.DROPDOWN,
                options = listOf("Pending (විසඳමින් පවතී)", "Completed (අවසන්)", "Referred (යොමු කළ)"))
        )
    ),

    // 05. රජයේ ඉඩම් තොරතුරු ලේඛනය
    RegisterModule(
        id = "land_gov",
        titleSi = "05. රජයේ ඉඩම් තොරතුරු ලේඛනය",
        titleEn = "05. Government Lands Information Register",
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

    // 06. බලපත්‍ර නිර්දේශ කිරීමේ ලේඛනය
    RegisterModule(
        id = "permit_recommendations",
        titleSi = "06. බලපත්‍ර නිර්දේශ කිරීමේ ලේඛනය",
        titleEn = "06. Permit Recommendations Register",
        category = "Land & Resources",
        icon = Icons.Default.FactCheck,
        iconRes = R.drawable.ic_round_permits,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("applicantName", "අයදුම්කරුගේ නම / Applicant Name", FieldType.TEXT, required = true),
            FieldSpec("applicantAddress", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("permitType", "බලපත්‍ර වර්ගය / Permit Type", FieldType.DROPDOWN, required = true,
                options = listOf("Timber Transport (දැව ප්‍රවාහන)", "Sand/Soil (වැලි / පස්)", "Tree Felling (ගස් කැපීම)", "Animal Transport (සත්ත්ව ප්‍රවාහන)", "Public Performance (ශබ්ද විකාශන/පොදු)", "Other (වෙනත්)")),
            FieldSpec("vehicleNo", "වාහන අංකය / Vehicle No", FieldType.TEXT),
            FieldSpec("quantity", "ප්‍රමාණය / Quantity", FieldType.TEXT),
            FieldSpec("date", "දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("destination", "ගමනාන්තය / Destination", FieldType.TEXT),
            FieldSpec("recommendationStatus", "නිර්දේශයේ තත්ත්වය / Status", FieldType.DROPDOWN,
                options = listOf("Recommended (නිර්දේශ කෙරේ)", "Rejected (ප්‍රතික්ෂේපිත)", "Pending Inspection (පරීක්ෂණ මට්ටමේ)")),
            FieldSpec("dsOfficeReceiptNo", "ප්‍රා.ලේ.කා රිසිට්පත් අංකය / DS Receipt No", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 07. රජයේ දීමනා හා රැකියා ලේඛනය
    RegisterModule(
        id = "gov_allowances",
        titleSi = "07. රජයේ දීමනා හා රැකියා ලේඛනය",
        titleEn = "07. Government Allowances & Employment",
        category = "Welfare & Social",
        icon = Icons.Default.VolunteerActivism,
        iconRes = R.drawable.ic_round_allowance,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("beneficiaryName", "ප්‍රතිලාභියාගේ නම / Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("allowanceType", "දීමනා වර්ගය / Allowance Type", FieldType.DROPDOWN,
                options = listOf("Aswasuma (අස්වැසුම)", "Elder Allowance (වැඩිහිටි දීමනාව)", "Disability Aid (ආබාධිත ආධාර)", "Kidney Disease Aid (වකුගඩු රෝගී ආධාර)", "Self-Employment (ස්වයං රැකියා සහන)", "Other (වෙනත්)")),
            FieldSpec("monthlyAmount", "මාසික මුදල / Amount (LKR)", FieldType.NUMBER,
                validation = Validation(min = 0.0)),
            FieldSpec("startDate", "ආරම්භක දිනය / Start Date", FieldType.DATE),
            FieldSpec("employmentStatus", "රැකියා ස්වභාවය / Employment", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 08. දිස්ත්‍රික් ආපදා කළමනාකරණ හා සහනාධාර ලේඛනය
    RegisterModule(
        id = "disaster_relief",
        titleSi = "08. ආපදා හා සහනාධාර ලේඛනය",
        titleEn = "08. Disaster Management & Relief Register",
        category = "Emergency & Welfare",
        icon = Icons.Default.HealthAndSafety,
        iconRes = R.drawable.ic_round_disaster,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("affectedPersonName", "විපතට පත් අයගේ නම / Affected Person", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("disasterType", "ආපදා වර්ගය / Disaster Type", FieldType.DROPDOWN, required = true,
                options = listOf("Flood (ගංවතුර)", "Fire (ගිනි ගැනීම්)", "Strong Winds (සුළි සුළං / තද සුළං)", "Drought (නියඟය)", "Landslide (නායයෑම්)", "Wildlife Damage (වන සතුන්ගෙන් වූ හානි)", "Other (වෙනත්)")),
            FieldSpec("incidentDate", "ආපදාව සිදුවූ දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("estimatedDamage", "ඇස්තමේන්තුගත හානිය / Est. Damage (LKR)", FieldType.NUMBER),
            FieldSpec("reliefReceivedDateAmount", "ලබාදුන් සහනාධාර හා දිනය", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 09. අධිකරණ, ළමා හා සමථ මණ්ඩල කටයුතු පිළිබඳ ලේඛනය
    RegisterModule(
        id = "judicial_mediation",
        titleSi = "09. අධිකරණ, ළමා හා සමථ මණ්ඩල ලේඛනය",
        titleEn = "09. Judicial, Child Care & Mediation Register",
        category = "Legal & Protection",
        icon = Icons.Default.Gavel,
        iconRes = R.drawable.ic_round_judicial,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("caseRefNo", "යොමු අංකය / Case Ref No", FieldType.TEXT),
            FieldSpec("category", "අංශය / Forum Category", FieldType.DROPDOWN, required = true,
                options = listOf("Mediation Board (සමථ මණ්ඩලය)", "Child Welfare & Protection (ළමා සුබසාධන)", "Magistrate Court (මහේස්ත්‍රාත් අධිකරණ)", "Maintenance & Family (නඩත්තු හා පවුල්)")),
            FieldSpec("complainantName", "පැමිණිලිකරුගේ නම / Complainant", FieldType.TEXT, required = true),
            FieldSpec("respondentName", "විත්තිකරුගේ නම / Respondent", FieldType.TEXT),
            FieldSpec("disputeNature", "ආරවුලේ ස්වභාවය / Dispute Nature", FieldType.MULTILINE, required = true),
            FieldSpec("hearingDate", "විභාග කළ දිනය / Hearing Date", FieldType.DATE),
            FieldSpec("actionTakenOrder", "ගත් ක්‍රියාමාර්ග / නියෝගය", FieldType.MULTILINE),
            FieldSpec("status", "තත්ත්වය / Status", FieldType.DROPDOWN,
                options = listOf("Settled (සමථ විය)", "Referred to Court (අධිකරණයට යොමු කළ)", "Under Inquiry (විභාග වෙමින් පවතී)")),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 10. ආයුර්වේද ලේඛනය
    RegisterModule(
        id = "ayurveda_health",
        titleSi = "10. ආයුර්වේද හා සෞඛ්‍ය ලේඛනය",
        titleEn = "10. Ayurveda & Community Health Register",
        category = "Health & Wellness",
        icon = Icons.Default.LocalHospital,
        iconRes = R.drawable.ic_round_ayurveda,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("patientOrProgramName", "නම / වැඩසටහනේ නම / Name", FieldType.TEXT, required = true),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("category", "සෞඛ්‍ය වැඩසටහන් වර්ගය", FieldType.DROPDOWN,
                options = listOf("Ayurvedic Treatment (ආයුර්වේද ප්‍රතිකාර)", "Dengue Inspection (ඩෙංගු පාලන)", "Health Clinic (සෞඛ්‍ය සායන)", "Traditional Herb Conservation (ඖෂධ සංරක්ෂණ)", "Other (වෙනත්)")),
            FieldSpec("date", "දිනය / Date", FieldType.DATE),
            FieldSpec("officerOrDoctorInCharge", "භාර වෛද්‍ය/නිලධාරී / In Charge", FieldType.TEXT),
            FieldSpec("details", "විස්තර / Description", FieldType.MULTILINE),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 11. වසම තුළ ක්‍රියාත්මක විවිධ සංවර්ධන ව්‍යාපෘති තොරතුරු ලේඛනය
    RegisterModule(
        id = "development_projects",
        titleSi = "11. වසමේ සංවර්ධන ව්‍යාපෘති ලේඛනය",
        titleEn = "11. Development Projects Register",
        category = "Development",
        icon = Icons.Default.Engineering,
        iconRes = R.drawable.ic_round_projects,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("projectName", "ව්‍යාපෘතියේ නම / Project Name", FieldType.TEXT, required = true),
            FieldSpec("location", "ස්ථානය / Location", FieldType.TEXT, required = true),
            FieldSpec("estimatedBudget", "ඇස්තමේන්තුගත මුදල / Budget (LKR)", FieldType.NUMBER),
            FieldSpec("implementingAgency", "ක්‍රියාත්මක කරන ආයතනය / Agency", FieldType.TEXT),
            FieldSpec("startDate", "ආරම්භක දිනය / Start Date", FieldType.DATE),
            FieldSpec("expectedEndDate", "අවසන් විය යුතු දිනය / Target Date", FieldType.DATE),
            FieldSpec("progressStatus", "ප්‍රගතිය / Status", FieldType.DROPDOWN,
                options = listOf("Planned (සැලසුම් කර ඇත)", "In Progress (ක්‍රියාත්මක වෙමින් පවතී)", "Completed (සම්පූර්ණයි)", "Suspended (තාවකාලිකව නැවතී ඇත)")),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 12. පදිංචිය භාරගැනීම් හා හැරයෑම් ලේඛනය, තාවකාලික පදිංචිකරුවන් පිළිබඳ තොරතුරු ලේඛනය
    RegisterModule(
        id = "residency_changes",
        titleSi = "12. පදිංචිය භාරගැනීම්, හැරයෑම් හා තාවකාලික පදිංචි",
        titleEn = "12. Residency Arrivals, Departures & Temp Residents",
        category = "Civil & Population",
        icon = Icons.Default.TransferWithinAStation,
        iconRes = R.drawable.ic_round_residence,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("fullName", "පුද්ගලයාගේ / ප්‍රධානියාගේ නම / Name", FieldType.TEXT, required = true),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC),
            FieldSpec("houseNo", "ගෘහ අංකය / House No", FieldType.TEXT),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("changeType", "පදිංචි වෙනස්වීම් වර්ගය", FieldType.DROPDOWN, required = true,
                options = listOf("Arrival / New Residency (පදිංචියට පැමිණීම)", "Departure / Leaving (වසමෙන් පිටවීම)", "Temporary Resident (තාවකාලික පදිංචිකරු)")),
            FieldSpec("eventDate", "සිදුවූ දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("previousOrNewAddress", "පෙර / අලුත් ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("reason", "හේතුව / Reason", FieldType.TEXT),
            FieldSpec("familyMembersCount", "සාමාජික ගණන / Family Members", FieldType.NUMBER),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 13. දිවුරුම් සහතික නිකුත් කිරීමේ ලේඛනය
    RegisterModule(
        id = "affidavits",
        titleSi = "13. දිවුරුම් සහතික නිකුත් කිරීමේ ලේඛනය",
        titleEn = "13. Affidavits Issued Register",
        category = "Legal & Protection",
        icon = Icons.Default.Description,
        iconRes = R.drawable.ic_round_affidavit,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("applicantName", "අයදුම්කරුගේ නම / Applicant Name", FieldType.TEXT, required = true),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC, required = true),
            FieldSpec("address", "ලිපිනය / Address", FieldType.MULTILINE),
            FieldSpec("affidavitPurpose", "දිවුරුම් සහතිකයේ කාරණය / Purpose", FieldType.TEXT, required = true),
            FieldSpec("issueDate", "නිකුත් කළ දිනය / Date Issued", FieldType.DATE, required = true),
            FieldSpec("certNo", "සහතික අංකය / Certificate No", FieldType.TEXT),
            FieldSpec("dsOfficeRef", "ප්‍රා.ලේ.කා යොමු අංකය / DS Ref", FieldType.TEXT),
            FieldSpec("remarks", "වෙනත් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 14. ව්‍යාපාර නාම ලියාපදිංචි කිරීම සඳහා නිර්දේශ කිරීම් ලේඛනය
    RegisterModule(
        id = "business_registrations",
        titleSi = "14. ව්‍යාපාර නාම ලියාපදිංචිය නිර්දේශ කිරීමේ ලේඛනය",
        titleEn = "14. Business Name Registration Recommendations",
        category = "Commerce & Trade",
        icon = Icons.Default.Storefront,
        iconRes = R.drawable.ic_round_business,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("businessNameAndAddress", "ව්‍යාපාරයේ නම හා ස්ථානයේ ලිපිනය", FieldType.MULTILINE, required = true),
            FieldSpec("ownerNameAddressPhoneNic", "හිමිකරුවන්ගේ නම / ලිපිනය / දුරකථන / ජා.හැ.අ", FieldType.MULTILINE, required = true),
            FieldSpec("businessNature", "ව්‍යාපාරයේ ස්වභාවය / Business Nature", FieldType.DROPDOWN, required = true,
                options = listOf("Sole Proprietorship (තනි පුද්ගල ව්‍යාපාර)", "Partnership (හවුල් ව්‍යාපාර)")),
            FieldSpec("startedDate", "ආරම්භ කල දිනය / Started Date", FieldType.DATE),
            FieldSpec("recommendedDateAndDsReceiptNo", "නිර්දේශ කල දිනය හා ප්‍රා.ලේ.කා රිසිට්පත් අංකය", FieldType.TEXT, required = true),
            FieldSpec("premisesOwnershipDetails", "ස්ථානයේ අයිතිය / පදිංචිය / වෙනත් ව්‍යාපාර විස්තර", FieldType.MULTILINE),
            FieldSpec("registrationNoAndDate", "ලියාපදිංචි අංකය හා දිනය / Reg No & Date", FieldType.TEXT),
            FieldSpec("applicantSignatureStatus", "අයදුම්කරුගේ අත්සන තහවුරුව / Signature Status", FieldType.DROPDOWN,
                options = listOf("Signed & Verified (අත්සන් කර තහවුරු විය)", "Pending Signature (අත්සන් බලාපොරොත්තුවෙන්)")),
            FieldSpec("remarks", "වෙනත් (වැරදි සහතික කිරීම්/නොගැලපීම් ආදිය)", FieldType.MULTILINE)
        )
    ),

    // 15. උපත් හා මරණ වාර්තා ලේඛනය
    RegisterModule(
        id = "birth_death_reports",
        titleSi = "15. උපත් හා මරණ වාර්තා ලේඛනය",
        titleEn = "15. Birth & Death Reports Register",
        category = "Civil & Population",
        icon = Icons.Default.EventAvailable,
        iconRes = R.drawable.ic_round_birth,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("reportType", "වාර්තා වර්ගය / Report Type", FieldType.DROPDOWN, required = true,
                options = listOf("Death Report (මරණ වාර්තාව)", "Birth Report (උපත් වාර්තාව)")),
            FieldSpec("subjectName", "මියගිය අයගේ / දරුවාගේ නම / Subject Name", FieldType.TEXT, required = true),
            FieldSpec("nicOrParentsInfo", "ජා.හැ.අ හෝ දෙමාපියන්ගේ විස්තර", FieldType.TEXT),
            FieldSpec("eventDate", "සිදුවූ දිනය (මරණය / උපත) / Date", FieldType.DATE, required = true),
            FieldSpec("eventPlace", "සිදුවූ ස්ථානය / Place", FieldType.TEXT, required = true),
            FieldSpec("causeOrBirthWeight", "මරණයට හේතුව / උපත් බර / Cause or Details", FieldType.TEXT),
            FieldSpec("reportNoAndDate", "වාර්තා අංකය හා දිනය / Report No & Date", FieldType.TEXT),
            FieldSpec("takenToRegistrarPersonNamePhone", "රෙජිස්ට්‍රාර් වෙත ගෙනයෑමට භාරගත් අය හා දුරකථනය", FieldType.TEXT),
            FieldSpec("remarks", "අත්සන / වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // 16. විශ්‍රාම වැටුප් ලේඛනය
    RegisterModule(
        id = "pension_registry",
        titleSi = "16. විශ්‍රාම වැටුප් ලේඛනය",
        titleEn = "16. Pensions Register (Public, Farmer & Social)",
        category = "Welfare & Social",
        icon = Icons.Default.Elderly,
        iconRes = R.drawable.ic_round_pension,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("pensionType", "විශ්‍රාම වැටුප් වර්ගය / Pension Type", FieldType.DROPDOWN, required = true,
                options = listOf("Public Pension (රාජ්‍ය විශ්‍රාම වැටුප්)", "Farmers Pension (ගොවි විශ්‍රාම වැටුප්)", "Social Security Pension (සමාජ ආරක්ෂණ විශ්‍රාම වැටුප්)")),
            FieldSpec("pensionNo", "විශ්‍රාම වැටුප් අංකය / Pension No", FieldType.TEXT, required = true),
            FieldSpec("pensionerName", "ලාභියාගේ නම / Pensioner Name", FieldType.TEXT, required = true),
            FieldSpec("addressAndHouseNo", "ලිපිනය හා ගෘහ අංකය / Address & House No", FieldType.MULTILINE),
            FieldSpec("nic", "ජා.හැ.අ / NIC", FieldType.NIC),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE),
            FieldSpec("paymentOfficeOrBankAcc", "මුදල් ලබාගන්නා තැපැල් කාර්යාලය / බැංකු ගිණුම", FieldType.TEXT),
            FieldSpec("heldDesignation", "දරන ලද තනතුර / Designation", FieldType.TEXT),
            FieldSpec("guardianNameAndAddress", "භාරකරුගේ නම හා ලිපිනය / Guardian Info", FieldType.MULTILINE),
            FieldSpec("maritalStatus", "තත්ත්වය / Marital Status", FieldType.DROPDOWN,
                options = listOf("Married (විවාහක)", "Widowed (වැන්දඹු)", "Single (තනිකඩ)")),
            FieldSpec("monthlyAmount", "මාසික මුදල / Monthly Amount (LKR)", FieldType.NUMBER),
            FieldSpec("dateReportedToDS", "ප්‍රා.ලේ.කා වාර්තා කළ දිනය / Reported Date", FieldType.DATE),
            FieldSpec("remarks", "වෙනත් (දායකත්ව විස්තර ආදී) / Remarks", FieldType.MULTILINE)
        )
    ),

    // 17. ගර්භණී මව්වරුන් සඳහා පෝෂණ දීමනාව ලබාදීමේ ලේඛනය
    RegisterModule(
        id = "maternity_nutrition",
        titleSi = "17. ගර්භණී මව්වරුන්ගේ පෝෂණ දීමනා ලේඛනය",
        titleEn = "17. Maternity Nutrition Allowance Register",
        category = "Welfare & Social",
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
            FieldSpec("clinicCardNo", "සායන කාඩ්පත් අංකය / Clinic Card No", FieldType.TEXT),
            FieldSpec("dateSentToDS", "ප්‍රා.ලේ.කා භාර දුන් දිනය / Sent to DS Date", FieldType.DATE),
            FieldSpec("registrationPeriod", "ලියාපදිංචි දිනය හා කාලසීමාව / Period", FieldType.TEXT),
            FieldSpec("voucherNo", "පෝෂණ වවුචර් අංකය / Voucher No", FieldType.TEXT),
            FieldSpec("voucherIssueDate", "වවුචරය භාරදුන් දිනය / Issued Date", FieldType.DATE),
            FieldSpec("recipientNameNic", "භාරගත් අය හා ජා.හැ.අ / Recipient NIC", FieldType.TEXT),
            FieldSpec("signatureStatus", "භාරගත් තත්ත්වය / Signature Status", FieldType.DROPDOWN,
                options = listOf("Signed / Received (භාරගන්නා ලදී)", "Pending Issue (නිකුත් කිරීමට ඇත)", "Transferred (වෙනත් වසමකට මාරු වූ)"))
        )
    ),

    // 18. ක්ෂේත්‍ර නිලධාරින් වසමට පැමිණීම සටහන් කිරීමේ අත්සන් සහතික කිරීමේ ලේඛනය
    RegisterModule(
        id = "officer_visits",
        titleSi = "18. ක්ෂේත්‍ර නිලධාරීන් පැමිණීමේ ලේඛනය",
        titleEn = "18. Field Officers Visit & Certification Register",
        category = "Administration",
        icon = Icons.Default.EventNote,
        iconRes = R.drawable.ic_round_contact,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("visitDate", "දිනය / Date", FieldType.DATE, required = true),
            FieldSpec("officerNameAndDesignation", "නිලධාරියාගේ නම හා තනතුර / Officer Name & Title", FieldType.TEXT, required = true),
            FieldSpec("department", "අයත් දෙපාර්තමේන්තුව / Department", FieldType.TEXT),
            FieldSpec("purposeOfVisit", "පැමිණි කාර්යය හා ස්ථානය / Purpose & Location", FieldType.TEXT, required = true),
            FieldSpec("inspectionNotesAndCertification", "පරීක්ෂණ සටහන් හා අත්සන් සහතිකය", FieldType.MULTILINE, required = true)
        )
    ),

    // 19. සමිති සංවිධාන / පොදු වැඩසටහන් තොරතුරු
    RegisterModule(
        id = "voluntary_orgs",
        titleSi = "19. සමිති සංවිධාන හා පොදු වැඩසටහන්",
        titleEn = "19. Community Organizations & Programs",
        category = "Community",
        icon = Icons.Default.Groups,
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

    // 20. විවිධ තොරතුරු ලේඛනය
    RegisterModule(
        id = "misc_info",
        titleSi = "20. විවිධ තොරතුරු ලේඛනය",
        titleEn = "20. Miscellaneous Information Register",
        category = "Administration",
        icon = Icons.Default.MenuBook,
        iconRes = R.drawable.ic_round_misc,
        fields = listOf(
            FieldSpec("serialNo", "අනු අංකය / Serial No", FieldType.TEXT, required = true),
            FieldSpec("categoryTitle", "තොරතුරු වර්ගය / Category Title", FieldType.TEXT, required = true),
            FieldSpec("subject", "විෂය මාතෘකාව / Subject Title", FieldType.TEXT, required = true),
            FieldSpec("date", "දිනය / Date", FieldType.DATE),
            FieldSpec("detailedDescription", "සවිස්තරාත්මක විස්තරය / Detailed Description", FieldType.MULTILINE, required = true),
            FieldSpec("actionTaken", "ක්‍රියාමාර්ග / Follow-up Actions", FieldType.MULTILINE),
            FieldSpec("remarks", "වෙනත් සටහන් / Remarks", FieldType.MULTILINE)
        )
    ),

    // CONTACT DIRECTORY MODULE
    RegisterModule(
        id = "contact",
        titleSi = "විමසීම් හා සබඳතා",
        titleEn = "Contact & Directory",
        category = "Directory",
        icon = Icons.Default.Phone,
        iconRes = R.drawable.ic_round_contact,
        fields = listOf(
            FieldSpec("contactName", "නම / Contact Person", FieldType.TEXT, required = true),
            FieldSpec("phone", "දුරකථන අංකය / Phone", FieldType.PHONE, required = true),
            FieldSpec("email", "විද්‍යුත් තැපෑල / Email", FieldType.TEXT),
            FieldSpec("department", "අංශය / Department", FieldType.DROPDOWN,
                options = listOf("Administration (පරිපාලන)", "Technical Support (තාක්ෂණික)", "Social Welfare (සමාජ සුබසාධන)", "Public Services (මහජන සේවා)", "Emergency Hotline (හදිසි ඇමතුම්)")),
            FieldSpec("message", "පණිවිඩය / Notes", FieldType.MULTILINE)
        )
    )
)
