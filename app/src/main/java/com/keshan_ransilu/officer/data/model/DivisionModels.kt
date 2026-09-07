package com.keshan_ransilu.officer.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

// ==========================================
// CORE POPULATION & CITIZEN MODELS
// ==========================================

@Serializable
data class PersonRecord(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val nic: String,
    val houseId: String? = null,
    val address: String = "",
    val phone: String = "",
    val dob: String = "",
    val gender: String = "Male",
    val occupation: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class HouseholdRecord(
    val id: String = UUID.randomUUID().toString(),
    val houseNo: String,
    val householderId: String? = null,
    val address: String = "",
    val gnDivision: String = "142 - Mahara Central",
    val memberCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ==========================================
// 20 GRAMA NILADHARI OFFICIAL REGISTERS
// ==========================================

// 01. මුදල් පොත (Cash Book Register)
@Serializable
data class CashBookRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val transactionType: String = "Collection (අය කිරීම)", // Collection or Disbursement
    val receiptNo: String = "",
    val payerPayeeNameAddress: String = "",
    val purpose: String,
    val received: Double = 0.0,
    val paid: Double = 0.0,
    val handedOverDate: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 02. විෂය ගොනු ලේඛනය (Subject Files Register)
@Serializable
data class SubjectFileRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val fileNo: String,
    val fileName: String,
    val startDate: String = "",
    val locationRack: String = "",
    val status: String = "Active (ක්‍රියාකාරී)",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 03. බඩු වවුචර් හා අංකිත ආකෘති ලේඛනය (Goods Voucher & Counterfoil Forms Register)
@Serializable
data class GoodsVoucherFormRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val formType: String, // උපපත්‍රිකා / අංකිත ආකෘති වර්ගය
    val acquiredDateAndIssuedBy: String = "",
    val printedNumberFrom: String = "",
    val printedNumberTo: String = "",
    val lastUsedDate: String = "",
    val disposedOrHandedOverDatePerson: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 04. ලැබෙන හා යවන ලිපි ලේඛනය (Inward & Outward Letters Register)
@Serializable
data class InwardOutwardLetterRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val letterType: String = "Inward (ලැබෙන ලිපි)", // Inward or Outward
    val date: String = "",
    val letterRefNoAndDate: String = "",
    val senderOrReceiver: String = "",
    val contentSummary: String = "",
    val actionTaken: String = "",
    val fileNo: String = "",
    val status: String = "Pending",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 05. රජයේ ඉඩම් තොරතුරු ලේඛනය (Government Lands Register)
@Serializable
data class GovLandRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val landName: String,
    val planNo: String = "",
    val fvpLotNo: String = "",
    val allocatedPurpose: String = "",
    val landNatureUsage: String = "",
    val extent: String = "",
    val boundaries: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 06. බලපත්‍ර නිර්දේශ කිරීමේ ලේඛනය (Permit Recommendation Register)
@Serializable
data class PermitRecommendationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicantName: String,
    val applicantAddress: String = "",
    val permitType: String = "Timber (දැව)", // Timber, Sand, Soil, Transport, Tree Felling, Public Performance
    val vehicleNo: String = "",
    val quantity: String = "",
    val date: String = "",
    val destination: String = "",
    val recommendationStatus: String = "Recommended (නිර්දේශ කෙරේ)",
    val dsOfficeReceiptNo: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 07. රජයේ දීමනා හා රැකියා ලේඛනය (Government Allowances, Welfare & Employment Register)
@Serializable
data class GovAllowanceRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val beneficiaryName: String,
    val houseNo: String = "",
    val nic: String = "",
    val phone: String = "",
    val allowanceType: String = "Welfare Allowance (සුබසාධන දීමනා)",
    val monthlyAmount: Double = 0.0,
    val startDate: String = "",
    val employmentStatus: String = "Self-Employed (ස්වයං රැකියා)",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 08. දිස්ත්‍රික් ආපදා කළමනාකරණ හා සහනාධාර ලේඛනය (Disaster Management & Relief Register)
@Serializable
data class DisasterReliefRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val affectedPersonName: String,
    val houseNo: String = "",
    val phone: String = "",
    val disasterType: String = "Flood (ගංවතුර)", // Flood, Fire, Drought, Wind, Landslide, Wildlife
    val incidentDate: String = "",
    val estimatedDamage: Double = 0.0,
    val reliefReceivedDateAmount: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 09. අධිකරණ, ළමා හා සමථ මණ්ඩල කටයුතු ලේඛනය (Judicial, Child Care & Mediation Register)
@Serializable
data class JudicialMediationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val caseRefNo: String = "",
    val category: String = "Mediation Board (සමථ මණ්ඩල)", // Mediation Board, Child Rights, Magistrate Court, Maintenance
    val complainantName: String,
    val respondentName: String = "",
    val disputeNature: String = "",
    val hearingDate: String = "",
    val actionTakenOrder: String = "",
    val status: String = "Settled (සමථයට පත්විය)",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 10. ආයුර්වේද හා සෞඛ්‍ය ලේඛනය (Ayurveda & Health Care Register)
@Serializable
data class AyurvedaHealthRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val patientOrProgramName: String,
    val houseNo: String = "",
    val phone: String = "",
    val category: String = "Ayurveda Treatment (ආයුර්වේද ප්‍රතිකාර)", // Ayurveda, Dengue Prevention, Clinic Program, Traditional Medicine
    val date: String = "",
    val officerOrDoctorInCharge: String = "",
    val details: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 11. වසමේ සංවර්ධන ව්‍යාපෘති තොරතුරු ලේඛනය (Development Projects Register)
@Serializable
data class DevelopmentProjectRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val projectName: String,
    val location: String = "",
    val estimatedBudget: Double = 0.0,
    val implementingAgency: String = "",
    val startDate: String = "",
    val expectedEndDate: String = "",
    val progressStatus: String = "In Progress (ක්‍රියාත්මක වෙමින් පවතී)",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 12. පදිංචිය භාරගැනීම්, හැරයෑම් හා තාවකාලික පදිංචිකරුවන්ගේ ලේඛනය (Residency Arrivals/Departures & Temporary Residents)
@Serializable
data class ResidencyChangeRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val fullName: String,
    val nic: String = "",
    val houseNo: String = "",
    val phone: String = "",
    val changeType: String = "Arrival (පදිංචියට පැමිණීම)", // Arrival, Departure, Temporary Resident
    val eventDate: String = "",
    val previousOrNewAddress: String = "",
    val reason: String = "",
    val familyMembersCount: Int = 1,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 13. දිවුරුම් සහතික නිකුත් කිරීමේ ලේඛනය (Affidavits Issued Register)
@Serializable
data class AffidavitIssueRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicantName: String,
    val nic: String = "",
    val address: String = "",
    val affidavitPurpose: String, // Reason for affidavit (Income, Non-Employment, Character, Loss of NIC)
    val issueDate: String = "",
    val certNo: String = "",
    val dsOfficeRef: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 14. ව්‍යාපාර නාම ලියාපදිංචි කිරීම සඳහා නිර්දේශ කිරීම් ලේඛනය (Business Name Registration Recommendation Register)
@Serializable
data class BusinessRegistrationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val businessNameAndAddress: String,
    val ownerNameAddressPhoneNic: String = "",
    val businessNature: String = "Sole Proprietorship (තනි පුද්ගල ව්‍යාපාර)", // Sole Proprietorship, Partnership
    val startedDate: String = "",
    val recommendedDateAndDsReceiptNo: String = "",
    val premisesOwnershipDetails: String = "",
    val registrationNoAndDate: String = "",
    val applicantSignatureStatus: String = "Signed (අත්සන් කෙරිණි)",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 15. උපත් හා මරණ වාර්තා ලේඛනය (Birth & Death Reports Register)
@Serializable
data class BirthDeathReportRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val reportType: String = "Death Report (මරණ වාර්තාව)", // Birth Report, Death Report
    val subjectName: String, // Deceased Name or Child Name
    val nicOrParentsInfo: String = "",
    val eventDate: String = "", // Date of death or birth
    val eventPlace: String = "", // Place of death or birth
    val causeOrBirthWeight: String = "",
    val reportNoAndDate: String = "",
    val takenToRegistrarPersonNamePhone: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 16. විශ්‍රාම වැටුප් ලේඛනය (Pensions Register: Public, Farmers, Social Security)
@Serializable
data class PensionRegistryRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val pensionType: String = "Public Pension (රාජ්‍ය විශ්‍රාම වැටුප්)", // Public Pension, Farmers Pension, Social Security Pension
    val pensionNo: String = "",
    val pensionerName: String,
    val addressAndHouseNo: String = "",
    val nic: String = "",
    val phone: String = "",
    val paymentOfficeOrBankAcc: String = "",
    val heldDesignation: String = "",
    val guardianNameAndAddress: String = "",
    val maritalStatus: String = "Married (විවාහක)",
    val monthlyAmount: Double = 0.0,
    val dateReportedToDS: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 17. ගර්භණී මව්වරුන් සඳහා පෝෂණ දීමනාව ලබාදීමේ ලේඛනය (Maternity Nutrition Allowance Register)
@Serializable
data class MaternityNutritionRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val motherName: String,
    val houseNo: String = "",
    val phone: String = "",
    val monthlyIncome: Double = 0.0,
    val familyMemberCount: Int = 1,
    val clinicCardNo: String = "",
    val dateSentToDS: String = "",
    val registrationPeriod: String = "",
    val voucherNo: String = "",
    val voucherIssueDate: String = "",
    val recipientNameNic: String = "",
    val signatureStatus: String = "Signed / Received (භාරගන්නා ලදී)",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 18. ක්ෂේත්‍ර නිලධාරීන් වසමට පැමිණීම සටහන් කිරීමේ අත්සන් සහතික කිරීමේ ලේඛනය (Field Officers Visit Register)
@Serializable
data class FieldOfficerVisitRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val visitDate: String = "",
    val officerNameAndDesignation: String,
    val department: String = "",
    val purposeOfVisit: String = "",
    val inspectionNotesAndCertification: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 19. සමිති සංවිධාන / පොදු වැඩසටහන් තොරතුරු ලේඛනය (Community Organizations & Public Programs Register)
@Serializable
data class CommunityOrgRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val orgNameStartedDate: String,
    val presidentNamePhone: String = "",
    val secretaryNamePhone: String = "",
    val treasurerNamePhone: String = "",
    val regNoDate: String = "",
    val memberCount: Int = 0,
    val purposeObjectives: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 20. විවිධ තොරතුරු ලේඛනය (Miscellaneous Information Register)
@Serializable
data class MiscInfoRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val categoryTitle: String = "General (පොදු තොරතුරු)",
    val subject: String,
    val date: String = "",
    val detailedDescription: String = "",
    val actionTaken: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ==========================================
// ADDITIONAL WELFARE & COMPATIBILITY MODELS
// ==========================================

@Serializable
data class AswasumaRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val beneficiaryName: String,
    val houseNo: String = "",
    val phone: String = "",
    val familyMemberCount: Int = 1,
    val categoryLevel: String = "Transitional",
    val startDate: String = "",
    val monthlyAidAmount: Double = 0.0,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class SamurdhiRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val beneficiaryName: String,
    val houseNo: String = "",
    val phone: String = "",
    val familyMemberCount: Int = 1,
    val allowanceAmount: Double = 0.0,
    val startDate: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class SeniorCitizenRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicationDate: String = "",
    val fullName: String,
    val houseNo: String = "",
    val phone: String = "",
    val nic: String = "",
    val dob: String = "",
    val dateSentToDS: String = "",
    val cardNoDate: String = "",
    val deliveredDate: String = "",
    val signatureStatus: String = "Delivered & Signed",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class ContactInquiryRecord(
    val id: String = UUID.randomUUID().toString(),
    val contactName: String,
    val phone: String,
    val email: String = "",
    val department: String = "Administration",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
