import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthProvider';
import { createItem } from '../services/itemService';
import ItemsShell from '../components/layout/ItemsShell';
import Header from '../components/layout/Header';
import Sidebar from '../components/sidebar/Sidebar';
import MenuDrawer from '../components/menu/MenuDrawer';
import ItemForm from '../components/items/ItemForm';
import '../components/layout/styles/MainContent.css';
import '../components/layout/styles/ItemsContent.css';

export default function CreateItem() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [drawerOpen, setDrawerOpen] = useState(false);

  if (!user.isSupplier) return <Navigate to="/" replace />;

  const handleSubmit = async (item) => {
    await createItem(item);
    navigate('/company-items');
  };

  return (
    <>
      <ItemsShell
        header={
          <Header
            onToggleMenu={() => setDrawerOpen((open) => !open)}
            onSearch={() => {}}
            isMenuOpen={drawerOpen}
          />
        }
        sidebar={<Sidebar isBuyer={user.isBuyer} isSupplier={user.isSupplier} />}
        main={
          <main className="main-content">
            <section className="items-content">
              <div className="items-content__header">
                <h2>Add new item</h2>
              </div>
              <ItemForm
                onSubmit={handleSubmit}
                onCancel={() => navigate('/company-items')}
                submitLabel="Create item"
              />
            </section>
          </main>
        }
      />
      <MenuDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} user={user} />
    </>
  );
}
