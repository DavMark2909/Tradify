import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthProvider'
import ProtectedRoute from './utils/ProtectedRoute'
import Home from './pages/Home'
import Onboarding from './pages/Onboarding'

function App() {

  return (
    <AuthProvider>
      <Routes>
        <Route path="/onboarding" element={<Onboarding />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Home />} />
        </Route>
      </Routes>
    </AuthProvider>
  )

}

export default App
