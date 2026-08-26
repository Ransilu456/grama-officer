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

// 5. රජයේ ඉඩම් ලේඛනය
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

// ඉඩම් හිමිකරුවන්ගේ ලේඛනය
@Serializable
data class LandAllotteeRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val allotteeName: String,
    val address: String = "",
    val nic: String = "",
    val phone: String = "",
    val grantPermitNoDate: String = "",
    val landName: String = "",
    val extent: String = "",
    val fvpNo: String = "",
    val lotNo: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// වාර්ෂික බලපත්‍රලාභීන්ගේ ලේඛනය - 75 ගොනුව
@Serializable
data class AnnualLandPermit75Record(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val permitHolderName: String,
    val houseNo: String = "",
    val address: String = "",
    val phone: String = "",
    val fvpNo: String = "",
    val planLotNo: String = "",
    val permitNo: String = "",
    val landName: String = "",
    val extent: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ඉඩම් ආරවුල් පිළිබඳ ලේඛනය
@Serializable
data class LandDisputeRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val complainantName: String,
    val respondentName: String = "",
    val date: String = "",
    val disputeNature: String = "",
    val inquiryDate: String = "",
    val actionsTaken: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// මිනුම් තොරතුරු ලේඛනය
@Serializable
data class LandSurveyRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val applicantName: String,
    val landName: String = "",
    val fvpPlanNo: String = "",
    val applicationDate: String = "",
    val dateSentToDS: String = "",
    val surveyedDate: String = "",
    val surveyingInstitution: String = "",
    val surveyReferenceNo: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// අස්වැසුම ලේඛනය (Aswasuma Welfare Registry)
@Serializable
data class AswasumaRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val beneficiaryName: String,
    val houseNo: String = "",
    val phone: String = "",
    val familyMemberCount: Int = 1,
    val categoryLevel: String = "Transitional", // Extreme Poor, Poor, Vulnerable, Transitional
    val startDate: String = "",
    val monthlyAidAmount: Double = 0.0,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// 17. පෝෂණ මල්ල (ගර්භණී මව්වරුන්ගේ පෝෂණ මල්ල ලේඛනය)
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
    val signatureStatus: String = "Received",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// සමෘද්ධි සහනාධාර ලේඛනය
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

// වැඩිහිටි හැඳුනුම්පත් ලේඛනය
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
    val signatureStatus: String = "Signed",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ආධාර ලාභීන් මිය ගිය අයගේ නාම ලේඛනය
@Serializable
data class DeceasedBeneficiaryRecord(
    val id: String = UUID.randomUUID().toString(),
    val serialNo: String = "",
    val deceasedName: String,
    val houseNo: String = "",
    val schemeType: String = "Aswasuma", // Aswasuma, Samurdhi, Senior, Nutrition
    val cardNo: String = "",
    val dateReportedToDS: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ජ්‍යෙෂ්ඨ නිලධාරීන්ගේ වසමට පැමිණීමේ සටහන්
@Serializable
data class OfficerVisitRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val officerNameDesignation: String,
    val purposeOfVisit: String = "",
    val inspectionNotes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ස්වේච්ඡා සංවිධාන හා පොදු වැඩසටහන්
@Serializable
data class VoluntaryOrgRecord(
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

// ලැබෙන/යවන ලිපි හා ඉල්ලීම්
@Serializable
data class LetterRequestRecord(
    val id: String = UUID.randomUUID().toString(),
    val refNo: String,
    val date: String = "",
    val subject: String,
    val fromTo: String = "",
    val status: String = "Pending", // Pending, Approved, In Progress, Completed, Rejected
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ද්‍රව්‍ය හා ප්‍රවාහන බලපත්‍ර
@Serializable
data class PermitRecord(
    val id: String = UUID.randomUUID().toString(),
    val holderId: String? = null,
    val item: String,
    val vehicleNo: String = "",
    val quantity: String = "",
    val date: String = "",
    val destination: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// මුදල් පොත
@Serializable
data class CashBookRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val received: Double = 0.0,
    val paid: Double = 0.0,
    val purpose: String,
    val receiptNo: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// උපත් වාර්තා
@Serializable
data class BirthRecord(
    val id: String = UUID.randomUUID().toString(),
    val childName: String,
    val fatherId: String? = null,
    val motherId: String? = null,
    val dob: String = "",
    val regNo: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// මරණ වාර්තා
@Serializable
data class DeathRecord(
    val id: String = UUID.randomUUID().toString(),
    val personId: String? = null,
    val dateOfDeath: String = "",
    val placeOfDeath: String = "",
    val cause: String = "",
    val certNo: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// කාර්මික සේවා
@Serializable
data class MechanicRecord(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val ratePerHour: Double = 0.0,
    val specialty: String = "Automotive Repair",
    val phone: String = "",
    val rating: Double = 5.0,
    val location: String = "",
    val availability: String = "Available",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// විමසීම් හා සබඳතා
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
