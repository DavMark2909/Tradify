import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';

export default function ProtectedRoute() {
    const { user } = useAuth();

    // If the backend returned null for the company profile/permissions,
    // force them to the onboarding screen.
    const needsOnboarding = !user?.companyName;

    if (needsOnboarding) {
        return <Navigate to="/onboarding" replace />;
    }

    return <Outlet />;
}