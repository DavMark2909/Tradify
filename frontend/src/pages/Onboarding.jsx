import { useAuth } from '../context/AuthProvider'

export default function Onboarding() {
  const { user } = useAuth()
  return (
    <div>
      <h1>Set up your company</h1>
      <p>Welcome, {user.name}. Onboarding form goes here.</p>
    </div>
  )
}
