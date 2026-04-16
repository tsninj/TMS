package mn.num.edu.user_service.domain.model;

/**
 * Системийн хэрэглэгчийн үүрэг (role).
 * Gateway дээр JWT token дахь role claim-аар хандалтын эрх тодорхойлогдоно.
 */
public enum SystemRole {

    ADMIN,       // Системийн админ - хэрэглэгч үүсгэх эрхтэй
    STUDENT,     // Оюутан
    TEACHER,     // Багш - үнэлгээ, тайлан
    DEPARTMENT,  // Тэнхим - ажлын урсгал, дүн, тогтоол
    GUEST        // Зочин (хязгаарлагдмал эрхтэй)

}