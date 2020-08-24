from password_strength import PasswordPolicy
from password_strength import PasswordStats

def policyChecker(passwordString):
    policy = PasswordPolicy.from_names(
		length=8,
		uppercase=2,
		numbers=2,
		special=2,
		nonletters=2
	)
	
    output = policy.test(passwordString)
    print(output)
    
def statChecker(passwordString):
	stats = PasswordStats(passwordString)
	strength = stats.strength()
	print(strength)
    
   
