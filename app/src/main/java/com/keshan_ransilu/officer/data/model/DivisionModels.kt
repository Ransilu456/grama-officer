package com.keshan_ransilu.officer.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

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

// Cash Book
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

// Subject Files
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

// Goods Voucher
@Serializable
data class GoodsVoucherFormRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val formType: String,
    val acquiredDateAndIssuedBy: String = "",
    val printedNumberFrom: String = "",
    val printedNumberTo: String = "",
    val lastUsedDate: String = "",
    val disposedOrHandedOverDatePerson: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Letters
@Serializable
data class InwardOutwardLetterRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val letterType: String = "Inward (ලැබෙන ලිපි)",
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

// Government Lands
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

// Permit Recommendation
@Serializable
data class PermitRecommendationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicantName: String,
    val applicantAddress: String = "",
    val permitType: String = "දැව", // Timber, Sand, Soil, Transport, Tree Felling, Public Performance
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

// Government Allowances, Welfare Allowances, and Housing Allowances
@Serializable
data class GovAllowanceRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val beneficiaryName: String,
    val houseNo: String = "",
    val nic: String = "",
    val phone: String = "",
    val allowanceType: String = "සුබසාධන දීමනා",
    val monthlyAmount: Double = 0.0,
    val startDate: String = "",
    val employmentStatus: String = "ස්වයං රැකියා",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Disaster
@Serializable
data class DisasterReliefRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val affectedPersonName: String,
    val houseNo: String = "",
    val phone: String = "",
    val disasterType: String = "ගංවතුර", // Flood, Fire, Drought, Wind, Landslide, Wildlife
    val incidentDate: String = "",
    val estimatedDamage: Double = 0.0,
    val reliefReceivedDateAmount: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Judicial, Child Care
@Serializable
data class JudicialMediationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val caseRefNo: String = "",
    val category: String = "සමථ මණ්ඩල", // Mediation Board, Child Rights, Magistrate Court, Maintenance
    val complainantName: String,
    val respondentName: String = "",
    val disputeNature: String = "",
    val hearingDate: String = "",
    val actionTakenOrder: String = "",
    val status: String = "සමථයට පත්විය",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Ayurveda & Health Care
@Serializable
data class AyurvedaHealthRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val patientOrProgramName: String,
    val houseNo: String = "",
    val phone: String = "",
    val category: String = "ආයුර්වේද ප්‍රතිකාර", // Ayurveda, Dengue Prevention, Clinic Program, Traditional Medicine
    val date: String = "",
    val officerOrDoctorInCharge: String = "",
    val details: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Development Projects
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
    val progressStatus: String = "ක්‍රියාත්මක වෙමින් පවතී",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Residency Arrivals/Departures
@Serializable
data class ResidencyChangeRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val fullName: String,
    val nic: String = "",
    val houseNo: String = "",
    val phone: String = "",
    val changeType: String = "පදිංචියට පැමිණීම", // Arrival, Departure, Temporary Resident
    val eventDate: String = "",
    val previousOrNewAddress: String = "",
    val reason: String = "",
    val familyMembersCount: Int = 1,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Affidavits Issued
@Serializable
data class AffidavitIssueRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicantName: String,
    val nic: String = "",
    val address: String = "",
    val affidavitPurpose: String, // Income, Non-Employment, Character, Loss of NIC
    val issueDate: String = "",
    val certNo: String = "",
    val dsOfficeRef: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Business Name Registration Recommendation
@Serializable
data class BusinessRegistrationRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val businessNameAndAddress: String,
    val ownerNameAddressPhoneNic: String = "",
    val businessNature: String = "තනි පුද්ගල ව්‍යාපාර", // Sole Proprietorship, Partnership
    val startedDate: String = "",
    val recommendedDateAndDsReceiptNo: String = "",
    val premisesOwnershipDetails: String = "",
    val registrationNoAndDate: String = "",
    val applicantSignatureStatus: String = "අත්සන් කෙරිණි",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Birth & Death Reports
@Serializable
data class BirthDeathReportRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val reportType: String = "මරණ වාර්තාව", // Birth Report, Death Report
    val subjectName: String,
    val nicOrParentsInfo: String = "",
    val eventDate: String = "",
    val eventPlace: String = "",
    val causeOrBirthWeight: String = "",
    val reportNoAndDate: String = "",
    val takenToRegistrarPersonNamePhone: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Pensions Register:
@Serializable
data class PensionRegistryRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val pensionType: String = "රාජ්‍ය විශ්‍රාම වැටුප්", // Public Pension, Farmers Pension, Social Security Pension
    val pensionNo: String = "",
    val pensionerName: String,
    val addressAndHouseNo: String = "",
    val nic: String = "",
    val phone: String = "",
    val paymentOfficeOrBankAcc: String = "",
    val heldDesignation: String = "",
    val guardianNameAndAddress: String = "",
    val maritalStatus: String = "විවාහක",
    val monthlyAmount: Double = 0.0,
    val dateReportedToDS: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Maternity Nutrition Allowance
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
    val signatureStatus: String = "භාරගන්නා ලදී",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Field Officers Visit
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

// Community Organizations
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

// Miscellaneous Information
@Serializable
data class MiscInfoRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val categoryTitle: String = "පොදු තොරතුරු",
    val subject: String,
    val date: String = "",
    val detailedDescription: String = "",
    val actionTaken: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)


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
