@{
    RootModule        = 'Keen.Git.psm1'
    ModuleVersion     = '0.1.0'
    GUID              = '30cd722f-ccb0-46a3-a1da-d09194ec5954'
    Author            = 'Ignacio Slater-Muñoz'
    CompanyName       = 'KEEN-OP'
    Copyright         = '(c) Ignacio Slater-Muñoz. All rights reserved.'
    FunctionsToExport = @('Invoke-GitCheckout')
    CmdletsToExport   = @()
    VariablesToExport = @()
    AliasesToExport   = @('Git-Checkout')

    PrivateData = @{
        PSData = @{
            Tags       = @('git', 'checkout', 'version-control')
            LicenseUri = 'https://opensource.org/licenses/BSD-2-Clause'
            ProjectUri = 'https://github.com/keen-op'
        }
    }
}
