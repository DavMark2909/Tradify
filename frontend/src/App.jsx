import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthProvider'
import ProtectedRoute from './utils/ProtectedRoute'
import Home from './pages/Home'
import Items from './pages/Items'
import CreateItem from './pages/CreateItem'
import Onboarding from './pages/Onboarding'

function App() {

  return (
    <AuthProvider>
      <Routes>
        <Route path="/onboarding" element={<Onboarding />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Home />} />
          <Route path="/company-items" element={<Items type="company" />} />
          <Route path="/company-items/new" element={<CreateItem />} />
          <Route path="/saved-items" element={<Items type="saved" />} />
        </Route>
      </Routes>
    </AuthProvider>
  )

}

export default App
