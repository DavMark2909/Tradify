import { useAuth } from '../context/AuthProvider'

export default function Home() {
  const { user } = useAuth()
  return (
    <div>
      <h1>Welcome, {user.name} {user.lastName}</h1>
      <p>Company: {user.companyName}</p>
    </div>
  )
}
