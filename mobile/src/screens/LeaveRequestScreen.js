import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  TextInput,
  Alert,
  ActivityIndicator,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const LeaveRequestScreen = ({ route, navigation }) => {
  const employeeFromParams = route.params?.employee;
  const employeeId = route.params?.employeeId || employeeFromParams?.id || '';

  const [type, setType] = useState('ANNUAL'); // ANNUAL/SICK/OTHER
  const [startDate, setStartDate] = useState(''); // YYYY-MM-DD
  const [endDate, setEndDate] = useState(''); // YYYY-MM-DD
  const [reason, setReason] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const validate = () => {
    if (!employeeId) {
      Alert.alert('Validasi', 'Employee ID tidak ditemukan. Buka dari halaman Employee.');
      return false;
    }
    if (!type) {
      Alert.alert('Validasi', 'Jenis cuti harus dipilih.');
      return false;
    }
    if (!startDate || !endDate) {
      Alert.alert('Validasi', 'Tanggal mulai dan akhir wajib diisi (YYYY-MM-DD).');
      return false;
    }
    if (endDate < startDate) {
      Alert.alert('Validasi', 'Tanggal akhir tidak boleh sebelum tanggal mulai.');
      return false;
    }
    if (!reason.trim()) {
      Alert.alert('Validasi', 'Alasan cuti wajib diisi.');
      return false;
    }
    return true;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setSubmitting(true);
    try {
      const payload = { employeeId, type, startDate, endDate, reason };
      await apiService.requestLeave(payload);
      try { Alert.alert('Sukses', 'Pengajuan cuti berhasil dikirim.'); } catch (_) {}
      navigation.goBack();
    } catch (err) {
      const msg = typeof err === 'string' ? err : (err?.message || 'Gagal mengajukan cuti');
      try { Alert.alert('Error', msg); } catch (_) { console.log('Leave request error:', err); }
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Icon name="arrow-back" size={24} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Leave Request</Text>
        <View style={{ width: 24 }} />
      </View>

      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Employee Info */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Employee</Text>
          <View style={styles.infoCard}>
            <View style={styles.row}>
              <Text style={styles.label}>Employee ID</Text>
              <Text style={styles.value}>{employeeId || '-'}</Text>
            </View>
            {employeeFromParams ? (
              <View style={styles.row}>
                <Text style={styles.label}>Name</Text>
                <Text style={styles.value}>{employeeFromParams.firstName} {employeeFromParams.lastName}</Text>
              </View>
            ) : null}
          </View>
        </View>

        {/* Form */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Request Details</Text>

          <View style={styles.inputGroup}>
            <Text style={styles.inputLabel}>Type</Text>
            <View style={styles.typeRow}>
              {['ANNUAL', 'SICK', 'OTHER'].map((t) => (
                <TouchableOpacity
                  key={t}
                  style={[styles.typeChip, type === t && styles.typeChipActive]}
                  onPress={() => setType(t)}
                >
                  <Text style={[styles.typeChipText, type === t && styles.typeChipTextActive]}>
                    {t}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          </View>

          <View style={styles.inputRow}>
            <View style={styles.inputHalf}>
              <Text style={styles.inputLabel}>Start Date (YYYY-MM-DD)</Text>
              <TextInput
                style={styles.textInput}
                placeholder="e.g. 2025-09-16"
                value={startDate}
                onChangeText={setStartDate}
                autoCapitalize="none"
              />
            </View>
            <View style={styles.inputHalf}>
              <Text style={styles.inputLabel}>End Date (YYYY-MM-DD)</Text>
              <TextInput
                style={styles.textInput}
                placeholder="e.g. 2025-09-18"
                value={endDate}
                onChangeText={setEndDate}
                autoCapitalize="none"
              />
            </View>
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.inputLabel}>Reason</Text>
            <TextInput
              style={[styles.textInput, { height: 100, textAlignVertical: 'top' }]}
              placeholder="Describe your reason..."
              multiline
              value={reason}
              onChangeText={setReason}
            />
          </View>

          <TouchableOpacity
            style={[styles.submitButton, { opacity: submitting ? 0.7 : 1 }]}
            onPress={handleSubmit}
            disabled={submitting}
          >
            {submitting ? (
              <ActivityIndicator color="#fff" />
            ) : (
              <>
                <Icon name="send-outline" size={20} color="#fff" />
                <Text style={styles.submitButtonText}>Submit Request</Text>
              </>
            )}
          </TouchableOpacity>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f8f9fa' },
  header: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#007bff', padding: 15, paddingTop: 20 },
  backButton: { marginRight: 15 },
  headerTitle: { flex: 1, fontSize: 18, fontWeight: 'bold', color: '#fff' },
  scrollView: { flex: 1 },
  section: { padding: 15 },
  sectionTitle: { fontSize: 18, fontWeight: 'bold', color: '#333', marginBottom: 12 },
  infoCard: { backgroundColor: '#fff', borderRadius: 10, padding: 12, elevation: 2, shadowColor: '#000', shadowOffset: { width: 0, height: 1 }, shadowOpacity: 0.08, shadowRadius: 2 },
  row: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 6 },
  label: { fontSize: 12, color: '#666' },
  value: { fontSize: 14, color: '#333', fontWeight: '500' },
  inputGroup: { marginBottom: 12 },
  inputLabel: { fontSize: 12, color: '#666', marginBottom: 6 },
  inputRow: { flexDirection: 'row', justifyContent: 'space-between' },
  inputHalf: { flex: 1, marginRight: 6 },
  textInput: { backgroundColor: '#fff', borderRadius: 10, borderWidth: 1, borderColor: '#e9ecef', paddingHorizontal: 12, height: 44 },
  typeRow: { flexDirection: 'row' },
  typeChip: { backgroundColor: '#f8f9fa', borderRadius: 16, paddingHorizontal: 12, paddingVertical: 8, marginRight: 8, borderWidth: 1, borderColor: '#e9ecef' },
  typeChipActive: { backgroundColor: '#007bff', borderColor: '#007bff' },
  typeChipText: { fontSize: 12, color: '#666' },
  typeChipTextActive: { color: '#fff', fontWeight: 'bold' },
  submitButton: { marginTop: 10, backgroundColor: '#007bff', borderRadius: 10, paddingVertical: 14, alignItems: 'center', flexDirection: 'row', justifyContent: 'center' },
  submitButtonText: { color: '#fff', fontWeight: 'bold', marginLeft: 8 },
});

export default LeaveRequestScreen;
