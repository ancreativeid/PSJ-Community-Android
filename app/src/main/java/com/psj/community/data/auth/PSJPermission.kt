package com.psj.community.data.auth

import com.psj.community.data.model.UserAccount

object PSJPermission {

    // PEMERIKSAAN ROLE

    private fun isSuperAdmin(account: UserAccount): Boolean {
        return account.role.equals("super_admin", ignoreCase = true)
    }

    private fun isAdmin(account: UserAccount): Boolean {
        return account.role.equals("admin", ignoreCase = true)
    }

    private fun isMember(account: UserAccount): Boolean {
        return account.role.equals("member", ignoreCase = true) ||
                account.role.equals("anggota", ignoreCase = true)
    }

    private fun isPSJ006(account: UserAccount): Boolean {
        return account.memberId.equals("PSJ-006", ignoreCase = true)
    }

    private fun isActive(account: UserAccount): Boolean {
        return account.status.equals("active", ignoreCase = true)
    }

    // HAK AKSES UMUM

    fun canViewAll(account: UserAccount): Boolean {
        return isActive(account)
    }

    fun canManageAll(account: UserAccount): Boolean {
        return isActive(account) && isSuperAdmin(account)
    }

    // MENU UMUM

    fun canManageGeneralMenu(account: UserAccount): Boolean {
        return isActive(account) &&
                (isSuperAdmin(account) || isAdmin(account))
    }

    // KAS DIGITAL

    fun canManageKas(account: UserAccount): Boolean {
        return isActive(account) &&
                (
                    isSuperAdmin(account) ||
                    (isAdmin(account) && isPSJ006(account))
                )
    }

    fun canAddKas(account: UserAccount): Boolean {
        return canManageKas(account)
    }

    fun canEditKas(account: UserAccount): Boolean {
        return canManageKas(account)
    }

    fun canDeleteKas(account: UserAccount): Boolean {
        return canManageKas(account)
    }

    // TAMBAH DATA UMUM

    fun canAddGeneralData(account: UserAccount): Boolean {
        return canManageGeneralMenu(account)
    }

    // EDIT DATA UMUM

    fun canEditGeneralData(account: UserAccount): Boolean {
        return canManageGeneralMenu(account)
    }

    // HAPUS DATA UMUM

    fun canDeleteGeneralData(account: UserAccount): Boolean {
        return canManageGeneralMenu(account)
    }

    // PROFIL ANGGOTA

    fun canEditOwnProfile(
        account: UserAccount,
        targetMemberId: String
    ): Boolean {

        if (!isActive(account)) return false

        if (isSuperAdmin(account) || isAdmin(account)) {
            return true
        }

        return isMember(account) &&
                account.memberId.isNotBlank() &&
                account.memberId.equals(
                    targetMemberId,
                    ignoreCase = true
                )
    }

    // PENDAFTARAN ANGGOTA

    fun canManageOwnRegistration(
        account: UserAccount,
        targetMemberId: String
    ): Boolean {

        if (!isActive(account)) return false

        if (isSuperAdmin(account) || isAdmin(account)) {
            return true
        }

        return isMember(account) &&
                account.memberId.isNotBlank() &&
                account.memberId.equals(
                    targetMemberId,
                    ignoreCase = true
                )
    }

    // LABEL ROLE

    fun getRoleLabel(account: UserAccount): String {

        return when {
            !isActive(account) -> "AKUN NONAKTIF"

            isSuperAdmin(account) -> "SUPER ADMIN"

            isAdmin(account) && isPSJ006(account) ->
                "ADMIN BENDAHARA"

            isAdmin(account) -> "ADMIN"

            isMember(account) -> "ANGGOTA"

            else -> "ROLE TIDAK DIKENALI"
        }
    }
}
