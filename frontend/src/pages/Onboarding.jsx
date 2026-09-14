import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';
import api from '../api';
import './Onboarding.css';

export default function Onboarding() {
  const { user, refreshUser } = useAuth();
  const navigate = useNavigate();

  const [step, setStep] = useState('choice'); // 'choice' | 'seller-form'
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const [sectors, setSectors] = useState([]);
  const [sectorsError, setSectorsError] = useState(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [sectorId, setSectorId] = useState('');

  useEffect(() => {
    if (step !== 'seller-form') return;
    api.get('/sector')
      .then((res) => setSectors(res.data))
      .catch(() => setSectorsError('Could not load sectors. Please try again later.'));
  }, [step]);

  const finish = async () => {
    await refreshUser();
    navigate('/', { replace: true });
  };

  const handleBuyer = async () => {
    setSubmitting(true);
    setError(null);
    try {
      await api.post('/company/create-default-buyer');
      await finish();
    } catch {
      setError('Could not complete sign-up as a buyer. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleSellerSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await api.post('/company/create', {
        name,
        description,
        sector: Number(sectorId),
        isSupplier: true,
        isConsumer: false,
        isLogistics: false,
      });
      await finish();
    } catch {
      setError('Could not create your company. Please check your details and try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="onboarding-backdrop">
      <div className="onboarding-card">
        <h1>Set up your company</h1>
        <p>Welcome, {user.name}. Tell us a bit about your business.</p>

        {step === 'choice' && (
          <div className="onboarding-choice">
            <button type="button" disabled={submitting} onClick={handleBuyer}>
              I&apos;m a buyer
            </button>
            <button type="button" disabled={submitting} onClick={() => setStep('seller-form')}>
              I&apos;m a seller
            </button>
          </div>
        )}

        {step === 'seller-form' && (
          <form onSubmit={handleSellerSubmit} className="onboarding-form">
            <label>
              Company name
              <input value={name} onChange={(e) => setName(e.target.value)} required />
            </label>
            <label>
              Description
              <textarea value={description} onChange={(e) => setDescription(e.target.value)} />
            </label>
            <label>
              Sector
              <select value={sectorId} onChange={(e) => setSectorId(e.target.value)} required>
                <option value="" disabled>Select a sector</option>
                {sectors.map((s) => (
                  <option key={s.id} value={s.id}>{s.name}</option>
                ))}
              </select>
            </label>
            {sectorsError && <p className="onboarding-error">{sectorsError}</p>}
            <div className="onboarding-form-actions">
              <button type="button" disabled={submitting} onClick={() => setStep('choice')}>
                Back
              </button>
              <button type="submit" disabled={submitting}>
                Create company
              </button>
            </div>
          </form>
        )}

        {error && <p className="onboarding-error">{error}</p>}
      </div>
    </div>
  );
}
